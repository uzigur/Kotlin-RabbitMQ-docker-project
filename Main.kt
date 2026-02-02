package org.example
import dev.kourier.amqp.connection.amqpConfig
import dev.kourier.amqp.connection.createAMQPConnection
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

import kotlinx.coroutines.*

//creating a new consumer,a topic exchange
suspend fun createExchange(coroutineScope: CoroutineScope){
    coroutineScope.launch{receiveLogsTopic(coroutineScope, listOf("Uzi.*","uzi.*"))}
    coroutineScope.launch{receiveLogsTopic(coroutineScope, listOf("Tal.*","tal.*"))}
    coroutineScope.launch{receiveLogsTopic(coroutineScope, listOf("Aviv.*","aviv.*"))}
}

suspend fun sendMessagesFromUserInput(coroutineScope: CoroutineScope) {
    delay(1000)//give the consumer a second to init the queues before starting send messages
    val config = amqpConfig { server { host = System.getenv("RABBIT_HOST") ?: "localhost"} }
    val connection = createAMQPConnection(coroutineScope, config)
    val channel = connection.openChannel()
    while (coroutineScope.isActive) { //while the coroutine still active it will keep asking for messages to send
        println("-----New messages-----")
        println("enter the roting key:")
        val routineKey = readln()
        //need checks
        println("enter your message:")
        val message = readln()
        emitLogTopic(coroutineScope, channel,routineKey, message)
        delay(100)//gives time to message arriving the queue before sending another messages
    }
    channel.close()
    connection.close()
}

fun randomPeople():String{
    val routineKeys = listOf("uzi.king", "Tal.buz", "Aviv.buz")
    return routineKeys.random()
}

suspend fun sendRandomMessages(coroutineScope: CoroutineScope){
    delay(1000)//give the consumer a second to init the queues before starting send messages
    val config = amqpConfig { server { host = "localhost"} }
    val connection = createAMQPConnection(coroutineScope, config)
    val channel = connection.openChannel()
    for(i in 1..10000/250){
        delay(1000)
        for(j in 1..250){
            emitLogTopic(coroutineScope, channel, randomPeople(), j.toString())
            delay(1)//for make sure it's arrived to the consumer
        }
    }
    channel.close()
    connection.close()
    //delay(100000)
}

fun main() = runBlocking {
    val consumerJob = launch(Dispatchers.Default) { createExchange(this) }
    val producerJob = launch(Dispatchers.IO) { sendMessagesFromUserInput(this) } //change to sendMessagesFromUserInput for be from user input
    //wait for the producer ended before closing the queue and exit the program
    producerJob.join()
    // Cancel consumer after testing
    consumerJob.cancelAndJoin()
}