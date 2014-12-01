% function [indices]=extract_keyframes(video)
clear;clc;
tic;
if(~exist('video', 'var'))
    video = 'C:\Videos\2\godfather - 1.avi';
end
%How many bins for the HSV histogram
h_bins=16;
s_bins=4;
v_bins=4;
number_of_bins=h_bins+s_bins+v_bins;
threshold=.06; %Adjust to get more/less frames
N=12;  %window size


%will read in frames_a_time frames at a time

start_index=1;
frames_a_time = 100; %will read in 100 frames at a time
first_iteration=1;
stop_flag=0;
j=1;
iteration=0;
frame_number=N+1;
while(~stop_flag)    
    %Read in movie
    iteration=iteration+1;
    cd('mmread');
    movie = mmread(video, [start_index: start_index + frames_a_time - 1], [], false, true);
    start_index = start_index + frames_a_time;
%     movie=mmread(video, [start_index: start_index+499], [], false, true);
%     start_index=start_index+frames_a_time;
    frames=movie.frames;
    
    clear movie; %for memory management
    
    number_of_frames=length(frames);
    if(number_of_frames<frames_a_time) %then we've reached the end of the video
        stop_flag=1;
    end
    
    cd('..');
    cd('colorspace');
    
    %make HSV histograms for every image   
    histograms=zeros(number_of_frames, number_of_bins);
    for i=1:number_of_frames
        hsv_image=colorspace('RGB->HSV', frames(i).cdata);
        h=hsv_image(:,:,1);
        s=hsv_image(:, :,2);
        v=hsv_image(:,:,3);
        histograms(i,:)=[imhist(h, h_bins)', imhist(s, s_bins)', imhist(v, v_bins)'];
    end
    
    if(~first_iteration)
        histograms=[saved_frames ; histograms];
    else
        %matrix for time N, where N is the window size
        S=svd(histograms( 1 : N, : ) );
        previous_rank=length(find( S/S(1)>threshold ));
        
        %Matrix for time N+1
        S=svd(histograms( 2 : (N+1), : ));
        next_rank=length(find( S/S(1)>threshold));
        
        indices=[];
        possibility=0;
    end
    
    if(first_iteration)
        for t = 3 : number_of_frames-N+1
            rank=next_rank;
            
            S=svd(histograms(t:t+N-1, :));
            next_rank=length(find( S/S(1)>threshold));
            
            if( (rank>previous_rank))
                possibility=frame_number;
                
            end
            
            if(rank<next_rank && possibility~=0)
                indices(j)=possibility;
                possibility=0;
                j=j+1;
            end
%             sprintf('Frame Number=%d', frame_number)
            frame_number=frame_number+1;
            previous_rank=rank;
        end
    else %not the first iteration through the while loop
        for t = 1 : number_of_frames
            rank=next_rank;     
            S=svd(histograms(t:t+N-1, :));
            next_rank=length(find( S/S(1)>threshold));
            
            if( (rank>previous_rank))
                possibility=frame_number;
                
            end
            
            if(rank<next_rank && possibility~=0)
                indices(j)=possibility;
                possibility=0;
                j=j+1;
            end
            frame_number=frame_number+1;
            previous_rank=rank;
        end
    end
        
    
    saved_frames=[];
    
    %we need to save the last frames for use if the next loop
    %But only have to do this if this is not the last while loop iteration
    if(~stop_flag)
        saved_frames=histograms((frames_a_time-N+2):frames_a_time, :);
    end
    first_iteration=0;
    cd('..');
end
cd('mmread');
% key_frames = mmread(video, indices, [0, end_time], false, true);
key_frames = mmread(video, indices, [], false, true);
key_frames = key_frames.frames;
cd('..');
for i = size(key_frames, 2) : -1 : 1
    figure;
    imshow(key_frames(i).cdata);
end

sprintf('Time to compute keyframes: %d', toc)