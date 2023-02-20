package cc.allio.uno.stater.srs;

import cc.allio.uno.component.media.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 提供Srs Web-Hook Restful api，基于SRS4.0版本
 *
 * @author jiangwei
 * @date 2022/3/31 14:39
 * @since 1.0.6
 */
@Slf4j
@RequestMapping("/uno/srs/endpoint")
public class UnoSrsEndpoint implements MediaCallback, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void onEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * 当触发连接时回调，推流端连接时进行触发
     *
     * @param connectMap 连接数据信息
     */
    @PostMapping("/onConnect")
    public Integer onConnect(@RequestBody Map<String, Object> connectMap) {
        Connect connect = new Connect();
        connect.setHost(connectMap.get("vhost").toString());
        connect.setClientId(connectMap.get("client_id").toString());
        connect.setPullUrl(connectMap.get("tcUrl").toString());
        connect.setApp(connectMap.get("app").toString());
        connect.setAttributes(connectMap);
        onEvent(new ConnectEvent(connect));
        return 0;
    }

    /**
     * 当触发断开连接时回调
     *
     * @param closeMap 断开连接信息
     */
    @PostMapping("/onClose")
    public Integer onClose(@RequestBody Map<String, Object> closeMap) {
        Close close = new Close();
        close.setApp(closeMap.get("app").toString());
        close.setClientId(closeMap.get("client_id").toString());
        close.setHost(closeMap.get("vhost").toString());
        close.setAttributes(closeMap);
        onEvent(new CloseEvent(close));
        return 0;
    }

    /**
     * 当开始推流时回调
     *
     * @param publishMap 推流数据信息
     */
    @PostMapping("/onPublish")
    public Integer onPublish(@RequestBody Map<String, Object> publishMap) {
        Publish publish = new Publish();
        publish.setClientId(publishMap.get("client_id").toString());
        publish.setApp(publishMap.get("app").toString());
        publish.setClientId(publishMap.get("client_id").toString());
        publish.setStream(publishMap.get("stream").toString());
        publish.setAttributes(publishMap);
        onEvent(new PublishEvent(publish));
        return 0;
    }

    /**
     * 当结束推流时的回调
     *
     * @param unPublishMap 结束推流的数据
     */
    @PostMapping("/onUnPublish")
    public Integer onUnPublish(@RequestBody Map<String, Object> unPublishMap) {
        UnPublish unPublish = new UnPublish();
        unPublish.setApp(unPublishMap.get("app").toString());
        unPublish.setStream(unPublishMap.get("stream").toString());
        unPublish.setClientId(unPublishMap.get("client_id").toString());
        unPublish.setHost(unPublishMap.get("vhost").toString());
        unPublish.setAttributes(unPublishMap);
        onEvent(new UnPublishEvent(unPublish));
        return 0;
    }

    /**
     * 当客户端停止播放时进行的回调
     *
     * @param playMap 停止播放数据
     */
    @PostMapping("/onPlay")
    public Integer onPlay(@RequestBody Map<String, Object> playMap) {
        Play play = new Play();
        play.setApp(playMap.get("app").toString());
        play.setStream(playMap.get("stream").toString());
        play.setClientId(playMap.get("client_id").toString());
        play.setHost(playMap.get("vhost").toString());
        play.setAttributes(playMap);
        onEvent(new PlayEvent(play));
        return 0;
    }

    /**
     * 当客户端停止播放时进行的回调
     *
     * @param stopMap 停止播放数据
     */
    @PostMapping("/onStop")
    public Integer onStop(@RequestBody Map<String, Object> stopMap) {
        Stop stop = new Stop();
        stop.setApp(stopMap.get("app").toString());
        stop.setStream(stopMap.get("stream").toString());
        stop.setClientId(stopMap.get("client_id").toString());
        stop.setHost(stopMap.get("vhost").toString());
        stop.setAttributes(stopMap);
        onEvent(new StopEvent(stop));
        return 0;
    }

    /**
     * 当DVR录制关闭一个flv文件时回调
     *
     * @param dvrMap dvr数据
     */
    @PostMapping("/onDvr")
    public Integer onDvr(@RequestBody Map<String, Object> dvrMap) {
        Dvr dvr = new Dvr();
        dvr.setApp(dvrMap.get("app").toString());
        dvr.setStream(dvrMap.get("stream").toString());
        dvr.setClientId(dvrMap.get("client_id").toString());
        dvr.setHost(dvrMap.get("vhost").toString());
        dvr.setCwd(dvrMap.get("cwd").toString());
        dvr.setFile(dvrMap.get("file").toString());
        dvr.setAttributes(dvrMap);
        onEvent(new DvrEvent(dvr));
        return 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
