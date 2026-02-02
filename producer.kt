package org.example
import dev.kourier.amqp.BuiltinExchangeType
import dev.kourier.amqp.Properties
import dev.kourier.amqp.connection.amqpConfig
import dev.kourier.amqp.connection.createAMQPConnection
import dev.kourier.amqp.channel.AMQPChannel
import kotlinx.coroutines.CoroutineScope

suspend fun emitLogTopic(
    coroutineScope: CoroutineScope,
    channel: AMQPChannel,
    routingKey: String,
    message: String
) {

    channel.exchangeDeclare(
        "topic_logs",
        BuiltinExchangeType.TOPIC,
        durable = false,
        autoDelete = false,
        internal = false,
        arguments = emptyMap()
    )

    channel.basicPublish(
        message.toByteArray(),
        exchange = "topic_logs",
        routingKey = routingKey,
        properties = Properties()
    )
    println(" [x] Sent '$routingKey':'$message'")
}