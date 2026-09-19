package pims.config.view;

import pims.config.dao.MedicineDAO;
import pims.config.dao.SalesDAO;
import pims.config.model.CartItem;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class CashierPanel extends JPanel {
    private JTable medicineTable;
    private DefaultTableModel medicineModel;

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private ArrayList<CartItem> cart = new ArrayList<>();

    private JButton btnAddToCart;
    private JButton btnRemoveItem;
    private JButton btnClearCart;
    private JButton btnCheckout;

    private JLabel lblTotal;

    private JTextField txtQuantity;

    private final int loggedInUserId;

    public CashierPanel(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        setLayout(new BorderLayout());

        medicineModel = new DefaultTableModel();

        medicineModel.addColumn("ID");
        medicineModel.addColumn("Medicine");
        medicineModel.addColumn("Price");
        medicineModel.addColumn("Stock");

        medicineTable = new JTable(medicineModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setTopComponent(new JScrollPane(medicineTable));

        //create cart table
        cartModel = new DefaultTableModel();
        cartModel.addColumn("ID");
        cartModel.addColumn("Medicine");
        cartModel.addColumn("Price");
        cartModel.addColumn("Quantity");
        cartModel.addColumn("Total");

        cartTable = new JTable(cartModel);
        splitPane.setBottomComponent( new JScrollPane(cartTable));
        add(splitPane, BorderLayout.CENTER);

        //cashier controls
        JPanel bottomPanel = new JPanel();

        txtQuantity = new JTextField(5);
        btnAddToCart = new JButton("Add To Cart");
        btnRemoveItem = new JButton("Remove Item");
        btnClearCart = new JButton("Clear Cart");
        btnCheckout = new JButton("Checkout");
        lblTotal = new JLabel("Total: R0.00");

        bottomPanel.add(new JLabel("Qty"));
        bottomPanel.add(txtQuantity);
        bottomPanel.add(btnAddToCart);
        bottomPanel.add(btnRemoveItem);
        bottomPanel.add(btnClearCart);
        bottomPanel.add(btnCheckout);
        bottomPanel.add(lblTotal);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMedicines();

        //button events
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveItem.addActionListener(e -> removeItem());
        btnClearCart.addActionListener(e -> clearCart());
        btnCheckout.addActionListener(e -> checkout());
    }

    private void loadMedicines() {
        try {
            medicineModel.setRowCount(0);
            MedicineDAO dao = new MedicineDAO();
            ResultSet rs = dao.getAllMedicines();

            while (rs.next()) {
                medicineModel.addRow(
                        new Object[]{
                                rs.getInt("medicine_id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("quantity_in_stock")
                        }
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    //adding items to cart
    private void addToCart() {
        int row = medicineTable.getSelectedRow();

        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine.");
            return;
        }

        try {
            int medicineId = Integer.parseInt(medicineModel.getValueAt(row,0).toString());
            String name = medicineModel.getValueAt(row,1).toString();
            double price = Double.parseDouble(medicineModel.getValueAt(row,2).toString());
            int quantity = Integer.parseInt(txtQuantity.getText());

            CartItem item = new CartItem(medicineId, name, price, quantity);
            cart.add(item);

            cartModel.addRow(
                    new Object[]{
                            medicineId,
                            name,
                            price,
                            quantity,
                            item.getTotal()
                    });
            updateTotal();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    //updating items in cart
    private void updateTotal() {
        double total = 0;

        for(CartItem item : cart) {
            total += item.getTotal();
        }
        lblTotal.setText(String.format("Total: R%.2f", total));
    }

    //removing items from cart
    private void removeItem() {
        int row = cartTable.getSelectedRow();

        if(row == -1) {
            return;
        }
        cart.remove(row);
        cartModel.removeRow(row);

        updateTotal();
    }

    //clearing the entire cart
    private void clearCart(){
        cart.clear();
        cartModel.setRowCount(0);
        updateTotal();
    }

    private void checkout(){
        if(cart.isEmpty()){
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }
        try{
            double total = 0;

            for(CartItem item : cart){
                total += item.getTotal();
            }
            SalesDAO dao = new SalesDAO();

            int saleId = dao.createSale(total, loggedInUserId);

            for (CartItem item : cart){
                dao.addSaleItem(saleId, item.getMedicineId(), item.getQuantity(), item.getPrice());
                dao.reduceStock(item.getMedicineId(), item.getQuantity());
            }
            showReceipt(saleId, total);

            clearCart();
            loadMedicines();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    //receipt on display
    private void showReceipt(int saleId, double total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("HealthFirst Pharmacy\n\n");
        receipt.append("Sale ID: ").append(saleId).append("\n\n");

        for(CartItem item : cart) {
            receipt.append(item.getMedicineName());
            receipt.append("  x ");
            receipt.append(item.getQuantity());
            receipt.append("  = R");
            receipt.append(String.format("%.2f", item.getTotal()));
            receipt.append("\n");
        }

        receipt.append("\nTOTAL: R");
        receipt.append(String.format("%.2f", total));

        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
}