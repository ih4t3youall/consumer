package ar.com.sourcesistemas.consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver {

    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    // default broker URL is : tcp://localhost:61616"

    // Name of the queue we will receive messages from
    //private static String subject = "JCG_QUEUE";
    final Gson gson = new Gson();
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public MessageReceiver(String subject) throws JMSException {
        // Getting JMS connection from the server
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = null;
        connection = connectionFactory.createConnection();
        connection.start();

        // Creating session for seding messages
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        // Getting the queue 'JCG_QUEUE'
        Destination destination = session.createQueue(subject);

        // MessageConsumer is used for receiving (consuming) messages
        consumer = session.createConsumer(destination);

        // Here we receive the message.
        //connection.close();
    }


    public String receiveMessage() throws JMSException {
        Message message ;
        message =consumer.receive();
        if (message instanceof TextMessage) {
            return ((TextMessage)message).getText();
        }
        return null;
    }

}