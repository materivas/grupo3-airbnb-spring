document.addEventListener("DOMContentLoaded", () => {
  const input = document.getElementById("placeInput");
  const suggestionsList = document.getElementById("suggestions");

  let debounceTimer;

  input.addEventListener("input", () => {
    const query = input.value.trim();
    clearTimeout(debounceTimer);

    if (query.length === 0) {
      suggestionsList.innerHTML = "";
      suggestionsList.classList.remove("active");
      return;
    }

    debounceTimer = setTimeout(async () => {
      try {
        const res = await fetch(`/api/propiedades/lugares/buscar?query=${encodeURIComponent(query)}`);
        if (!res.ok) throw new Error("Error al buscar lugares");
        const lugares = await res.json();
        mostrarSugerencias(lugares);
      } catch (err) {
        console.error("Error:", err);
      }
    }, 300); // Espera 300 ms para no hacer muchas llamadas
  });

  function mostrarSugerencias(lugares) {
    suggestionsList.innerHTML = "";

    if (lugares.length === 0) {
      suggestionsList.classList.remove("active");
      return;
    }

    lugares.forEach(lugar => {
      const li = document.createElement("li");
      li.innerHTML = `<i class="fa-solid fa-location-dot"></i> ${lugar}`;
      li.addEventListener("click", () => {
        input.value = lugar;
        suggestionsList.innerHTML = "";
        suggestionsList.classList.remove("active");
      });
      suggestionsList.appendChild(li);
    });

    suggestionsList.classList.add("active");
  }

  // Cierra las sugerencias si se hace click afuera
  document.addEventListener("click", (e) => {
    if (!e.target.closest(".autocomplete-container")) {
      suggestionsList.innerHTML = "";
      suggestionsList.classList.remove("active");
    }
  });
});