document.addEventListener('DOMContentLoaded', async () => {
  const pathParts = window.location.pathname.split('/');
  const id = pathParts[pathParts.length - 1];
  

  if (!id || isNaN(id)) {
    document.body.innerHTML = "<p>Error: No se especificó una propiedad válida.</p>";
    return;
  }

  try {
    const res = await fetch(`/api/propiedades/${id}`);
    if (!res.ok) throw new Error("No se pudo cargar la propiedad");

    const prop = await res.json();

    // Título
    document.getElementById("titulo").textContent = prop.titulo;

    // Imágenes 
    const contImg = document.getElementById("imagenes");
    if (prop.imageUrls && prop.imageUrls.length > 0) {
      contImg.innerHTML = prop.imageUrls.map(url => 
        `<img src="${url}" alt="${prop.titulo}" class="property-image" 
              onerror="this.src='/images/placeholder.jpg'">`
      ).join("");
    } else {
      contImg.innerHTML = `<img src="/images/placeholder.jpg" alt="${prop.titulo}" class="property-image">`;
    }

    // Detalles
    document.getElementById("precio").textContent = `$${prop.precioPorNoche} por noche`;
    document.getElementById("habitaciones").textContent = prop.habitaciones;
    document.getElementById("banos").textContent = prop.banos;
    document.getElementById("huespedes").textContent = prop.huespedes;

    // Descripción
    document.getElementById("descripcion").textContent = prop.descripcion;

    // Reserva
     document.getElementById('reservarBtn').addEventListener('click', function() {
        if (id) {
            window.location.href = '/reservar/' + id;
        }
    });

    /* Botón compartir
    document.getElementById("share-btn").onclick = () => {
      if (navigator.share) {
        navigator.share({ 
          title: prop.titulo, 
          text: prop.descripcion,
          url: window.location.href 
        });
      } else {
        alert("Función de compartir no soportada en este navegador.");
      }
    };
    */
  } catch (err) {
    console.error("Error cargando propiedad:", err);
    document.body.innerHTML = "<p>Error cargando la propiedad. Verifica la consola para más detalles.</p>";
  }
});