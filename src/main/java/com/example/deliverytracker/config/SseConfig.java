package com.example.deliverytracker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
public class SseConfig{

    @Bean
    public SseEmitters sseEmitters() {
        return new SseEmitters();
    }

    public static class SseEmitters {
        private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

        public SseEmitter add(Long storeId, SseEmitter emitter) {
            this.emitters.put(storeId, emitter);

            emitter.onCompletion(() -> {
                log.info("SSE 연결 해제/완료: storeId={}", storeId);
                this.emitters.remove(storeId);
            });
            emitter.onTimeout(() -> {
                log.warn("SSE 연결 타임아웃: storeId={}", storeId);
                emitter.complete();
            });

            try {
                emitter.send(SseEmitter.event().name("connect").data("connected!"));
            } catch (IOException e) {
                log.error("초기 연결 데이터 전송 실패: storeId={}", storeId);
                this.emitters.remove(storeId);
            }

            return emitter;
        }

        public void sendToStore(Long storeId, String eventName, Object data) {
            SseEmitter emitter = emitters.get(storeId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                } catch (IOException e) {
                    log.error("SSE 전송 에러: storeId={}, message={}", storeId, e.getMessage());
                    emitter.complete();
                    this.emitters.remove(storeId);
                }
            } else {
                log.debug("해당 가게에 연결된 SSE 에미터가 없습니다: storeId={}", storeId);
            }
        }
    }
}
