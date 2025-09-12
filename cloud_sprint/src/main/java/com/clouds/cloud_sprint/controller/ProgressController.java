package com.clouds.cloud_sprint.controller;

import com.clouds.cloud_sprint.model.FileUploadProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/progress")
public class ProgressController {
    @Autowired
    private FileUploadProgressListener progressListener;
    @GetMapping(value = "/upload", produces =
            MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter trackUploadProgress() {
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                int lastPercent = -1;
                while (true) {
                    int percent = progressListener.getPercentComplete();
                    if (percent != lastPercent) {
                        String eventName = (percent >= 100) ? "complete" : "progress";
                        emitter.send(SseEmitter.event().data(percent).name(eventName));
                        lastPercent = percent;
                    }
                    if (percent >= 100) {
                        break;
                    }
                    Thread.sleep(100);
                }
                progressListener.reset();
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }
}