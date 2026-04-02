package com.nurse.scheduling.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 *
 * @author nurse-scheduling
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 消息队列名称
     */
    public static final String SCHEDULE_QUEUE = "schedule.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    /**
     * 交换机名称
     */
    public static final String SCHEDULE_EXCHANGE = "schedule.exchange";

    /**
     * 路由键
     */
    public static final String SCHEDULE_ROUTING_KEY = "schedule.routing.key";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routing.key";

    /**
     * 消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("初始化RabbitTemplate");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    /**
     * 监听器容器工厂配置
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange scheduleExchange() {
        return new DirectExchange(SCHEDULE_EXCHANGE, true, false);
    }

    /**
     * 声明排班队列
     */
    @Bean
    public Queue scheduleQueue() {
        return QueueBuilder.durable(SCHEDULE_QUEUE).build();
    }

    /**
     * 声明通知队列
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    /**
     * 绑定排班队列到交换机
     */
    @Bean
    public Binding scheduleBinding() {
        return BindingBuilder.bind(scheduleQueue())
                .to(scheduleExchange())
                .with(SCHEDULE_ROUTING_KEY);
    }

    /**
     * 绑定通知队列到交换机
     */
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(scheduleExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }
}
