import { apiGet } from "./api.js";

export async function initOrders() {
    const orders = await apiGet("/orders");

    const tbody = document.querySelector("#ordersTable tbody");
    tbody.innerHTML = "";

    orders.forEach(o => {
        tbody.innerHTML += `
            <tr>
                <td>${o.id}</td>
                <td>${o.product?.name || "-"}</td>
                <td>${o.quantity}</td>
                <td>₹${o.totalPrice}</td>
                <td>${o.user?.username}</td>
                <td>${o.orderDate.replace("T", " ")}</td>
            </tr>
        `;
    });
}
