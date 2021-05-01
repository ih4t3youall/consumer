package ar.com.sourcesistemas.consumer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Data")
public class Data {

    @Id
    @Column(name = "id")
    public long id;
    @Column(name = "numero")
    public long numero;
    @Column(name = "time")
    public String time;

    public Data(long id, String numero, String time){
        this.id = id;
        this.numero = Long.valueOf(numero);
        this.time = time;
    }

    public Data(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void copy(Data data){
        this.id = data.id;
        this.numero = data.numero;
        this.time = data.time;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("id: ")
                .append(this.id)
                .append("numero: ")
                .append(this.numero)
                .append("time: ")
                .append(time);
        return builder.toString();

    }
}