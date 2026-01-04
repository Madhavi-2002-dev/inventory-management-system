import { apiGet, apiPost, apiPut, apiDelete } from './api.js';

let products = [];
let editingId = null;

export function initProducts() {
    document.getElementById('btnAdd').addEventListener('click', () => openModal());
    document.getElementById('cancelBtn').addEventListener('click', closeModal);
    document.getElementById('saveBtn').addEventListener('click', saveProduct);
    document.getElementById('productSearch').addEventListener('input', renderTable);

    loadProducts();
}

// LOAD
async function loadProducts() {
    try {
        products = await apiGet('/products') || [];
        renderTable();
    } catch (err) {
        console.error(err);
        alert("Failed to load products");
    }
}

// RENDER TABLE
function renderTable() {
    const tbody = document.querySelector('#productsTable tbody');
    const q = document.getElementById('productSearch').value.toLowerCase();
    

    const filtered = products.filter(p => (p.name || '').toLowerCase().includes(q));

    tbody.innerHTML = filtered.map(p => `
        <tr>
           <td>${p.id}</td>
  <td>${p.category}</td>
  <td>${p.name}</td>
  <td>${p.quantity}</td>
  <td>${p.price}</td>
  <td>${p.actions}</td>
            <td>
                <button class="action-btn edit" data-id="${p.id}">Edit</button>
                <button class="action-btn del" data-id="${p.id}">Delete</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("productCount").textContent = `${filtered.length} products`;

    // Attach button events
    document.querySelectorAll(".action-btn.edit").forEach(b =>
        b.addEventListener("click", () => editProduct(b.dataset.id)));

    document.querySelectorAll(".action-btn.del").forEach(b =>
        b.addEventListener("click", () => deleteProduct(b.dataset.id)));
}

// OPEN ADD FORM
function openModal() {
    editingId = null;
    document.getElementById("modalTitle").textContent = "Add Product";
    setForm({ name: "", category: "", quantity: "", price: "" });
    document.getElementById("modal").classList.add("show");
}

// CLOSE FORM
function closeModal() {
    document.getElementById("modal").classList.remove("show");
}

// SET FORM VALUES
function setForm(p) {
    document.getElementById("pName").value = p.name || "";
    document.getElementById("pCategory").value = p.category || "";
    document.getElementById("pQty").value = p.quantity ?? "";
    document.getElementById("pPrice").value = p.price ?? "";
}

// SAVE PRODUCT
async function editProduct(id) {
    id = Number(id);
    const p = await apiGet(`/products/${id}`);
    editingId = id;
    document.getElementById("modalTitle").textContent = "Edit Product";
    setForm(p);
    document.getElementById("modal").classList.add("show");
}

async function deleteProduct(id) {
    id = Number(id);
    if (!confirm("Delete this product?")) return;
    await apiDelete(`/products/${id}`);
    loadProducts();
}

async function saveProduct() {
    const product = {
        name: document.getElementById("pName").value.trim(),
        category: document.getElementById("pCategory").value.trim(),
        quantity: Number(document.getElementById("pQty").value),
        price: Number(document.getElementById("pPrice").value)
    };

    if (!product.name || !product.category) {
        alert("Fill all fields");
        return;
    }

    if (editingId !== null) {
        await apiPut(`/products/${editingId}`, product);
    } else {
        await apiPost('/products', product);
    }

    closeModal();
    loadProducts();
}

window.editProduct = editProduct;
window.deleteProduct = deleteProduct;
window.openModal = openModal;
window.closeModal = closeModal;
window.saveProduct = saveProduct;
