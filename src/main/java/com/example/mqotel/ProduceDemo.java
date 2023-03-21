package com.example.mqotel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.rocketmqclient.v4_8.RocketMqTelemetry;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.awt.*;
import java.nio.charset.StandardCharsets;

public class ProduceDemo {


    public static final String MQ_OTEL_TEST_TOPIC ="mq-otel-test" ;
    public static final String MQ_OTEL_TEST_NAMESERVER ="127.0.0.1:9876";

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {

        DefaultMQProducer producer = new DefaultMQProducer("quickstart_product");
        producer.setNamesrvAddr(MQ_OTEL_TEST_NAMESERVER);
        producer.start();

        OpenTelemetry openTelemetry = OtelConfiguration.getOpenTelemetry();
        RocketMqTelemetry rocketMqTelemetry = RocketMqTelemetry.create(openTelemetry);
        producer.getDefaultMQProducerImpl().registerSendMessageHook(rocketMqTelemetry.newTracingSendMessageHook());

        Message message = new Message(MQ_OTEL_TEST_TOPIC, "TAGA", "Hello mq".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult);
        Thread.sleep(10000);
    }

}
