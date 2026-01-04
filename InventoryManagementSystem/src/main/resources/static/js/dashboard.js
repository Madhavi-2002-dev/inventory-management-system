console.log("Dashboard JS loaded");

import { apiGet } from "./api.js";

export async function loadDashboard() {

    const products = await apiGet("/products");
console.log("Products from API:", products);

    const orders = await apiGet("/orders");

    // 🔢 Total products
    document.getElementById("totalProducts").textContent = products.length;

    // 📦 Total stock (from PRODUCTS only)
    const totalStock = products.reduce((sum, p) => sum + p.quantity, 0);
    document.getElementById("totalStock").textContent = totalStock;

    // 🛒 Orders today
    const today = new Date().toISOString().split("T")[0];
    const todayOrders = orders.filter(o =>
        o.orderDate?.startsWith(today)
    );
    document.getElementById("ordersToday").textContent = todayOrders.length;

    // 💰 Revenue
    const revenue = orders.reduce((sum, o) => sum + o.totalPrice, 0);
    document.getElementById("revenue").textContent = "₹ " + revenue;

    // ❌ Out of stock products
    const outOfStock = products.filter(p => p.quantity === 0);
    document.getElementById("outOfStock").innerHTML =
  outOfStock.length
    ? outOfStock.map(p => `${p.name} (${p.quantity})`).join("<br>")
    : "None";

    // ⚠️ Low stock products (<5)
    const lowStock = products.filter(p => p.quantity > 0 && p.quantity <= 5);
    document.getElementById("lowStock").innerHTML =
  lowStock.length
    ? lowStock.map(p => `${p.name} (${p.quantity})`).join("<br>")
    : "None";


    const highStock = products.filter(p => p.quantity > 50);
    document.getElementById("highStock").innerHTML =
  highStock.length
    ? highStock.map(p => `${p.name} (${p.quantity})`).join("<br>")
    : "None";
}
