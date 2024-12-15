package lesson5;

public class Main {
        public static void main(String[] args) {
            BinaryTree tree = new BinaryTree();

            tree.add(10);
            tree.add(5);
            tree.add(15);
            tree.add(3);
            tree.add(7);
            tree.add(12);
            tree.add(18);

            System.out.println("Дерево:");
            tree.printTree();

            System.out.println("\nУдаляем узел 15:");
            tree.delete(15);
            tree.printTree();

            System.out.println("\nИщем узел 7:");
            TreeNode node = tree.findNode(7);
            System.out.println(node != null ? "Узел найден: " + node.value : "Узел не найден");
        }


}
