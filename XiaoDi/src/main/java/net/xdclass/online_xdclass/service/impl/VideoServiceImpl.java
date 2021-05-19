package net.xdclass.online_xdclass.service.impl;

import net.xdclass.online_xdclass.config.CacheKeyManager;
import net.xdclass.online_xdclass.model.entity.Video;
import net.xdclass.online_xdclass.model.entity.VideoBanner;
import net.xdclass.online_xdclass.mapper.VideoMapper;
import net.xdclass.online_xdclass.service.VideoService;
import net.xdclass.online_xdclass.utils.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private BaseCache baseCache;

    @Override
    public List<Video> listvideo() {
        try {
            Object cacheObj = baseCache.getTenMinuteCache().get(CacheKeyManager.INDEX_VIDEO_LIST,()->{
                List<Video> videoList = videoMapper.listvideo();
                return videoList;
            });
            if (cacheObj instanceof List){
                List<Video> videoList = (List<Video>)cacheObj;
                return videoList;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //可以返回兜底数据，业务系统降级
        return null;
    }

    @Override
    public List<VideoBanner> listBanner() {

        try {
            Object cacheObj = baseCache.getTenMinuteCache().get(CacheKeyManager.INDEX_BANNER_KEY,()->{
                List<VideoBanner> bannerList = videoMapper.listVideoBanner();
                System.out.println("从数据库里面找轮播图列表");
                return bannerList;
            });
            if (cacheObj instanceof List){
                List<VideoBanner> bannerList = (List<VideoBanner>)cacheObj;
                return bannerList;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Video findDetailById(int videoId) {

        //单独构建一个缓存key，每个视频的key是不一样的
        String videoCacheKey = String.format(CacheKeyManager.VIDEO_DETAIL, videoId);

        try {
            Object cacheObject = baseCache.getOneHourCache().get(videoCacheKey,()->{
                Video video = videoMapper.findDetailById(videoId);
                return video;
            });
            if (cacheObject instanceof Video){
                Video video = (Video)cacheObject;
                return video;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
