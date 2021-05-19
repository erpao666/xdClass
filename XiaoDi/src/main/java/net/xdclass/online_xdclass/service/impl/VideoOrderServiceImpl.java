package net.xdclass.online_xdclass.service.impl;

import net.xdclass.online_xdclass.exception.XDException;
import net.xdclass.online_xdclass.mapper.EpisodeMapper;
import net.xdclass.online_xdclass.mapper.PlayRecordMapper;
import net.xdclass.online_xdclass.mapper.VideoMapper;
import net.xdclass.online_xdclass.mapper.VideoOrderMapper;
import net.xdclass.online_xdclass.model.entity.Episode;
import net.xdclass.online_xdclass.model.entity.PlayRecord;
import net.xdclass.online_xdclass.model.entity.Video;
import net.xdclass.online_xdclass.model.entity.VideoOrder;
import net.xdclass.online_xdclass.service.VideoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private EpisodeMapper episodeMapper;

    @Autowired
    private PlayRecordMapper playRecordMapper;

    /**
     * 下单模块
     * 未来版本：优惠券抵扣、风控用户检测、生成订单基础信息、生成支付信息
     * @param userId
     * @param videoId
     * @return
     */
    @Override
    @Transactional
    public int save(int userId, int videoId) {
        // 判断是否已经购买
        VideoOrder videoOrder = videoOrderMapper.findByUserIdAndOrderIdAndState(userId,videoId,1);
        if (videoOrder != null){
            return 0;
        }
        Video video = videoMapper.findById(videoId);
        VideoOrder newVideoOrder = new VideoOrder();
        newVideoOrder.setCreateTime(new Date());
        newVideoOrder.setOutTradeNo(UUID.randomUUID().toString());
        newVideoOrder.setState(1);
        newVideoOrder.setTotalFee(video.getPrice());
        newVideoOrder.setUserId(userId);
        newVideoOrder.setVideoId(videoId);
        newVideoOrder.setVideoImg(video.getCoverImg());
        newVideoOrder.setVideoTitle(video.getTitle());
        int rows = videoOrderMapper.saveOrder(newVideoOrder);

        // 生成播放记录
        if (rows == 1){
            Episode episode = episodeMapper.findFirstEpisodeByVideoId(videoId);
            if (episode == null){
                throw new XDException(-1,"视频没有集信息，请运营人员检查");
            }

            PlayRecord playRecord = new PlayRecord();
            playRecord.setCreateTime(new Date());
            playRecord.setEpisodeId(episode.getId());
            playRecord.setCurrentNum(episode.getNum());
            playRecord.setUserId(userId);
            playRecord.setVideoId(videoId);
            playRecordMapper.saveRecord(playRecord);
        }

        return rows;
    }

    @Override
    public List<VideoOrder> listOrderByUserId(Integer userId) {
        return videoOrderMapper.listOrderByUserId(userId);
    }
}
