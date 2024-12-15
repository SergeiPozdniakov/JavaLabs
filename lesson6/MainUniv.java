package lesson6;

public class MainUniv {

    public static void main(String[] args) {

        Student Piter = new Student();
        Piter.age = 20;
        Piter.name = "Piter Pen";
        Piter.greeting();
        System.out.println(" I am " + Piter.name + " and " + Piter.age + " years old");


        Docent Ivan = new Docent();
        Ivan.age = 45;
        Ivan.name = "Ivan Bright";
        Ivan.greeting();
        System.out.println(" I am " + Ivan.name + " and " + Ivan.age + " years old");
    }
}
