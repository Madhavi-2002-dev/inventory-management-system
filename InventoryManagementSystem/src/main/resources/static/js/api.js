// js/api.js
 // change if your backend is on different host/port
const BASE_URL = "http://localhost:8080";

async function handleResponse(res) {
    if (!res.ok) {
        const text = await res.text();
        throw new Error(`HTTP ${res.status}: ${text}`);
    }
    return res.json();
}

export async function apiGet(url) {
    const res = await fetch(BASE_URL + url, {
        credentials: "same-origin"   // 🔴 REQUIRED
    });
    return handleResponse(res);
}

async function safeFetch(path, options = {}) {
  const res = await fetch(BASE_URL + path, options);
  if (!res.ok) {
    const text = await res.text().catch(()=> '');
    throw new Error(`HTTP ${res.status}: ${text || res.statusText}`);
  }
  const txt = await res.text().catch(()=> '');
  return txt ? JSON.parse(txt) : null;
}


export async function apiPost(url) {
    const res = await fetch(BASE_URL + url, {
        method: "POST",
        credentials: "same-origin"
    });
    return handle(res);
}
export async function apiPut(path, data) {
  return safeFetch(path, {
    method: 'PUT',
    headers: {'Content-Type':'application/json'},
    body: data && Object.keys(data).length ? JSON.stringify(data) : undefined
  });
}
export async function apiDelete(path) {
  return safeFetch(path, { method: 'DELETE' });
}
