package org.example
import dev.kourier.amqp.BuiltinExchangeType
import dev.kourier.amqp.connection.amqpConfig
import dev.kourier.amqp.connection.createAMQPConnection
import kotlinx.coroutines.CoroutineScope

suspend fun receiveLogsTopic(
    coroutineScope: CoroutineScope,
    bindingKeys: List<String>
) {
    val config = amqpConfig {
        server {
            host = System.getenv("RABBIT_HOST") ?: "localhost"
        }
    }

    val connection = createAMQPConnection(coroutineScope, config)
    val channel = connection.openChannel()

    channel.exchangeDeclare(
        "topic_logs",
        BuiltinExchangeType.TOPIC,
        durable = false,
        autoDelete = false,
        internal = false,
        arguments = emptyMap()
    )

    val queue = channel.queueDeclare(
        name = "",
        durable = false,
        exclusive = false, //so I can connect to it from the UI of rabbit and read messages
        autoDelete = true,
        arguments = emptyMap()
    )

    for (key in bindingKeys) {
        channel.queueBind(queue.queueName, "topic_logs", key)
    }

    println(" [*] Waiting for logs")

    val consumer = channel.basicConsume(queue.queueName, noAck = true)

    for (delivery in consumer) {
        println(
            " [x] Received '${delivery.message.routingKey}':'${
                delivery.message.body.decodeToString()
            }'"
        )
    }
}
