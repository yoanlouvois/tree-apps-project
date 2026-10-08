package App.AssociationManagement;
import App.AssociationMember.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import others.Tree;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


public class Visit {

    private static final AtomicInteger count = new AtomicInteger(0);
    int id ;
    {
        id = count.incrementAndGet();
    }
    private final double cout;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private final LocalDate date;

    @JsonIgnoreProperties({"genre","espece","circonference","hauteur","stade_de_developpement","latitude","longitude","remarquable","dateClassification"})
    private Tree tree;

    @JsonProperty("compte_rendu")
    private String compteRendu;

    private Member member;

    private boolean visiteDefrayee;


    public Visit(double cout, LocalDate date, Tree tree,Member member, boolean visiteDefrayee) {
        this.cout = cout;
        this.date = date;
        this.tree = tree;
        this.member = member;
        this.visiteDefrayee = visiteDefrayee;
        this.compteRendu = "";
    }

    public Visit(){
        this.cout = 0;
        this.id=0;
        this.date = LocalDate.now();
        this.tree = new Tree();
        this.compteRendu = "";
        this.member = new Member();
        this.visiteDefrayee = false;
    }


    //getters
    public double getCout(){
        return cout;
    }
    public LocalDate getDate(){
        return date;
    }
    public Tree getTree(){
        return tree;
    }
    public String getCompteRendu(){
        return compteRendu;
    }
    public int getId(){
        return id;
    }
    public Member getMember(){
        return member;
    }

    public boolean getVisiteDefrayee(){
        return visiteDefrayee;
    }



}

