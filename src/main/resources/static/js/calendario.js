document.addEventListener("DOMContentLoaded", function () {

    const today = new Date();
	const nextMonth = new Date(today.getFullYear(), today.getMonth() + 1, 1);

    // Inicializar calendario Check-in
    const checkInCalendar = flatpickr("#checkIn", {
        dateFormat: "Y-m-d",
        minDate: today,
        locale: "es",
        onChange: function(selectedDates) {
            if (selectedDates.length > 0) {
                // Ajusta la fecha mínima del check-out
                checkOutCalendar.set("minDate", selectedDates[0]);
            }
        }
    });

	// Inicializar calendario Check-out
	const checkOutCalendar = flatpickr("#checkOut", {
	    dateFormat: "Y-m-d",
	    minDate: nextMonth,
	    locale: "es"
	});

});