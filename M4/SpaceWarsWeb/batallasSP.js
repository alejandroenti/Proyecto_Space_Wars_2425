function mostrarBatalla() {
    const num = document.getElementById("numeroBatalla").value.trim();

    if (num === "") {
        alert("Por favor, introduce un número de batalla.");
        return;
    }

    const archivo = `${num}.html`;  

    fetch(archivo)
        .then(res => {
            if (res.ok) {
                document.getElementById("visorBatalla").src = archivo;
            } else {
                alert(`La batalla número ${num} no existe.`);
                document.getElementById("visorBatalla").src = "";
            }
        })
        .catch(() => {
            alert("Error al intentar cargar la batalla.");
        });
}
