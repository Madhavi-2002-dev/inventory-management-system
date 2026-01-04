// js/stocks.js
import { apiGet, apiPost, apiDelete } from "./api.js";

let allStocks = [];

// Initialize
export async function initStocks() {
  try {
    await loadProductDropdown();
    await loadStocks();
    setupModalControls();
  } catch(err) {
    console.error('initStocks error:', err);
    alert('Failed to initialize stocks page: ' + err.message);
  }
}

// Load Stocks Table
export async function loadStocks() {
  try {
    allStocks = await apiGet("/stocks") || [];

    const tbody = document.querySelector("#stocksTable tbody");
    tbody.innerHTML = "";

    allStocks.forEach(s => {
      const last = s.lastUpdated ? String(s.lastUpdated).replace("T", " ") : "-";
      const tr = document.createElement("tr");

      tr.innerHTML = `
        <td>${s.product?.id ?? "-"}</td>
        <td>${s.quantity ?? 0}</td>
        <td>${last}</td>
        <td>
          <button class="action-btn del" data-id="${s.id}">Delete</button>
        </td>
      `;
      tbody.appendChild(tr);
    });

    // attach delete handlers
    document.querySelectorAll('.action-btn.del').forEach(b => {
      b.addEventListener('click', e => deleteStock(e.target.dataset.id));
    });

    document.getElementById("stockCount").textContent = `Total: ${allStocks.length}`;
  } catch(err) {
    console.error('loadStocks error:', err);
    alert('Could not load stocks: ' + err.message);
  }
}

// Load Product Dropdown (for Add Stock Modal)
export async function loadProductDropdown() {
  try {
    const products = await apiGet("/products") || [];
    const dropdown = document.getElementById("productSelect");
    if (!dropdown) return;
    dropdown.innerHTML = `<option value="">-- Select Product --</option>`;
    products.forEach(p => {
      const option = document.createElement("option");
      option.value = p.id;
      option.textContent = `${p.name} (ID: ${p.id}, Qty: ${p.quantity})`;
      dropdown.appendChild(option);
    });
  } catch(err) {
    console.error('loadProductDropdown error:', err);
    alert('Could not load products for dropdown: ' + err.message);
  }
}

// Save Stock (calls backend endpoint that accepts productId & quantity via query params)
export async function saveStock() {
    const productId = document.getElementById("productSelect").value;
    const quantity = document.getElementById("stockQuantity").value;

    if (!productId || !quantity) {
        alert("Please select a product and enter quantity!");
        return;
    }

    const payload = {
        product: { id: Number(productId) },
        quantity: Number(quantity)
    };

    await apiPost(`/stocks`, {
    product: { id: productId },
    quantity: parseInt(quantity)
});


    alert("Stock added successfully!");
    closeModal();
    await loadStocks();
    await loadProductDropdown();
}


// Delete Stock
export async function deleteStock(id) {
  try {
    if (!confirm("Delete this stock entry?")) return;
    await apiDelete(`/stocks/${id}`);
    await loadStocks();
  } catch(err) {
    console.error('deleteStock error:', err);
    alert('Delete failed: ' + err.message);
  }
}

// Modal open/close functions
function setupModalControls() {
  const btn = document.getElementById("btnAddStock");
  if (btn) btn.addEventListener('click', openModal);

  const modal = document.getElementById("modalStock");
  if (modal) modal.addEventListener('click', (e) => {
    // close when clicking on background (not on inner card)
    if (e.target === modal) closeModal();
  });
}

function openModal() {
  const modal = document.getElementById("modalStock");
  if (modal) modal.classList.add('show');
}

function closeModal() {
  const modal = document.getElementById("modalStock");
  if (modal) modal.classList.remove('show');
  const q = document.getElementById("stockQuantity");
  const dd = document.getElementById("productSelect");
  if (q) q.value = "";
  if (dd) dd.value = "";
}

// functions that are called from inline HTML onclick must be on window
window.saveStock = saveStock;
window.cancelStock = closeModal;
window.deleteStock = deleteStock;
window.openModal = openModal;
window.closeModal = closeModal;
