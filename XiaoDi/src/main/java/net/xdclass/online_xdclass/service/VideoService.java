package net.xdclass.online_xdclass.service;

import net.xdclass.online_xdclass.model.entity.Video;
import net.xdclass.online_xdclass.model.entity.VideoBanner;

import java.util.List;

public interface VideoService {

    //视频列表
    List<Video> listvideo();

    //轮播图列表
    List<VideoBanner> listBanner();

    //视频详情
    Video findDetailById(int videoId);

}
