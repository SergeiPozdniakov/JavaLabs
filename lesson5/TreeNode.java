package lesson5;

class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    class BinaryTree {
        private TreeNode root;

        public BinaryTree() {
            this.root = null;
        }

        // Метод для добавления узла
        public void add(int value) {
            root = addRecursive(root, value);
        }

        private TreeNode addRecursive(TreeNode current, int value) {
            if (current == null) {
                return new TreeNode(value);
            }

            if (value < current.value) {
                current.left = addRecursive(current.left, value);
            } else if (value > current.value) {
                current.right = addRecursive(current.right, value);
            }

            return current;
        }

        // Метод для удаления узла
        public void delete(int value) {
            root = deleteRecursive(root, value);
        }

        private TreeNode deleteRecursive(TreeNode current, int value) {
            if (current == null) {
                return null;
            }

            if (value == current.value) {
                // Узел найден, теперь его нужно удалить
                if (current.left == null && current.right == null) {
                    return null;
                }
                if (current.right == null) {
                    return current.left;
                }
                if (current.left == null) {
                    return current.right;
                }
                int smallestValue = findSmallestValue(current.right);
                current.value = smallestValue;
                current.right = deleteRecursive(current.right, smallestValue);
                return current;
            }

            if (value < current.value) {
                current.left = deleteRecursive(current.left, value);
                return current;
            }

            current.right = deleteRecursive(current.right, value);
            return current;
        }

        private int findSmallestValue(TreeNode root) {
            return root.left == null ? root.value : findSmallestValue(root.left);
        }

        // Метод для поиска узла по значению
        public TreeNode findNode(int value) {
            return findNodeRecursive(root, value);
        }

        private TreeNode findNodeRecursive(TreeNode current, int value) {
            if (current == null) {
                return null;
            }
            if (value == current.value) {
                return current;
            }
            return value < current.value
                    ? findNodeRecursive(current.left, value)
                    : findNodeRecursive(current.right, value);
        }

        // Метод для графического вывода дерева
        public void printTree() {
            printTreeRecursive(root, "", true);
        }

        private void printTreeRecursive(TreeNode node, String prefix, boolean isLeft) {
            if (node != null) {
                System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.value);
                printTreeRecursive(node.left, prefix + (isLeft ? "│   " : "    "), true);
                printTreeRecursive(node.right, prefix + (isLeft ? "│   " : "    "), false);
            }
        }
    }


