package ar.com.sourcesistemas.consumer;

import ar.com.sourcesistemas.consumer.entities.Data;
import ar.com.sourcesistemas.consumer.persistence.HibernateUtil;
import ar.com.sourcesistemas.consumer.sockets.ServerSocketImpl;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.jms.JMSException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private MessageReceiver messageReceiver;
    private Gson gson = new Gson();
    private boolean isFirstMessage = true;
    private Data firstMessage = new Data();
    private Data lastMessage = new Data();
    private Data data;
    private static String subject = "JCG_QUEUE"; // Queue Name.You can create any/many queue names as per your requirement.
    private Session session;


    public static void main(String[] args) {


        if (args.length == 0 || args.length > 1){
            System.out.println("no args provided");
            System.exit(1);
        }
        new Main(args);
    }

    public void waitKickIn(String [] args){
        try {
            new ServerSocketImpl(Integer.parseInt(args[0]));
        } catch (IOException e) {
            System.exit(2);
            e.printStackTrace();
        }
    }

    public Main(String[] args){
        HibernateUtil hibernate = null;
        hibernate = new HibernateUtil();
        session = hibernate.getSessionFactory().openSession();
        System.out.println("starting app");
        waitKickIn(args);
        try {
            messageReceiver = new MessageReceiver(subject);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("messageReceiver started....");
        doWork();
    }

    public void isWorkDone(){

        //int ID = 200;
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Data> criteriaQuery = criteriaBuilder.createQuery(Data.class);
        Root<Data> root = criteriaQuery.from(Data.class);
        //criteriaQuery.select(root.get("id"));
        //criteriaQuery.where(criteriaBuilder.equal(root.get("id"), ID));

        Query<Data> query = session.createQuery(criteriaQuery);

        List<Data>results = query.getResultList();
        for(Data data : results){
            System.out.println("Name: " + data.toString());
        }
    }

    public void doWork(){
        session.beginTransaction();
            try {
                while(true) {
                    String messageReceived = messageReceiver.receiveMessage();
                    if (messageReceived != null) {
                        data = gson.fromJson(messageReceived, Data.class);
                        session.save(data);
                        if (isFirstMessage){
                            isFirstMessage = false;
                            firstMessage.copy(data);
                        }
                        System.out.println(data.id);
                        if (data.id == -1){
                            lastMessage.copy(data);
                            break;
                        }
                    }
                }

                System.out.println("firstmessage: ");
                System.out.println(firstMessage.toString());
                System.out.println("lastMessage: ");
                System.out.println(lastMessage.toString());
                long diff = (Long.valueOf(lastMessage.time) - Long.valueOf(firstMessage.time));
                System.out.println("time in seconds: "+ diff/1_000_000_000);
                System.out.println("time in millis: "+ diff);
                System.out.println("printing final");
                session.flush();
                isWorkDone();
            } catch (JMSException e) {
                e.printStackTrace();
            }
    }
}
