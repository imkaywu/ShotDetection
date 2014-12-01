video_dir = 'D:\Videos\videos\';
video_name = 'scent of a woman - 3';
video_format = '.avi';
dir = [video_dir, video_name, video_format];

frame_dir = 'D:\Videos\video frames\';
if(~exist([frame_dir, video_name], 'dir'))
    mkdir([frame_dir, video_name]);
end

start_index = 1;
frames_a_time = 100;
stop_flag = 0;

while (~stop_flag)
    cd('mmread');
    clip = mmread(dir, [start_index: start_index + frames_a_time - 1], [], false, true);
    start_index = start_index + frames_a_time;
    frames = clip.frames;
    clear movie;
    
    number_of_frames = length(frames);
    if(number_of_frames < frames_a_time) %then we've reached the end of the video
        stop_flag = 1;
    end
    
    for i = 1 : number_of_frames
        imwrite(frames(i).cdata, [frame_dir, video_name, '\frame_', num2str(i), '.jpg']);
    end
end