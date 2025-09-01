document.addEventListener('DOMContentLoaded', async () => {
  const contenedor = document.getElementById('contenedor-propiedades');

  try {
    const respuesta = await fetch('/api/propiedades');
    if (!respuesta.ok) throw new Error('No se pudieron cargar las propiedades');

    const propiedades = await respuesta.json();

    if (propiedades.length === 0) {
      contenedor.innerHTML = '<p>No hay propiedades disponibles.</p>';
      return;
    }

    propiedades.forEach(prop => {
      const card = document.createElement('div');
      card.className = 'card';
      
      card.innerHTML = `
        <img src="${prop.mainImageUrl || '/images/placeholder.jpg'}" 
             alt="${prop.titulo}" 
             onerror="this.src='/images/placeholder.jpg'">
        <div class="card-content">
          <div class="card-header">
            <span>${prop.ubicacion}</span>
          </div>
          <a href="/propiedad/${prop.id}" class="card-title-link">
            <h3 class="card-title">${prop.titulo}</h3>
          </a>
          <div class="card-price">
            <strong>$${prop.precioPorNoche}</strong> por noche
          </div>
          <div class="card-guests">
            <small>Hasta ${prop.huespedes} huéspedes</small>
          </div>
        </div>
      `;

	  card.querySelector('.card-title-link').removeAttribute('href');
	  card.addEventListener('click', () => {
	    window.location.href = `/propiedad/${prop.id}`;
      });

      contenedor.appendChild(card);
    });

  } catch (error) {
    console.error('Error cargando propiedades:', error);
    contenedor.innerHTML = '<p>No se pudieron cargar las propiedades.</p>';
  }
});