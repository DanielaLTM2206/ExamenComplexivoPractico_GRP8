// Diálogos propios, validación de formularios y resumen de facturación del cliente web
(function () {
    "use strict";

    // ---------- Diálogo propio (reemplaza a alert/confirm del navegador) ----------
    const fondo = document.getElementById("dialogo");
    const caja = fondo.querySelector(".dialogo");
    const titulo = document.getElementById("dialogo-titulo");
    const mensaje = document.getElementById("dialogo-mensaje");
    const icono = document.getElementById("dialogo-icono");
    const aceptar = document.getElementById("dialogo-aceptar");
    const cancelar = document.getElementById("dialogo-cancelar");

    function abrir(textoTitulo, textoMensaje, tipo, conCancelar) {
        return new Promise(function (resolver) {
            titulo.textContent = textoTitulo;
            mensaje.textContent = textoMensaje;
            caja.classList.toggle("error", tipo === "error");
            icono.textContent = tipo === "error" ? "!" : (tipo === "pregunta" ? "?" : "✓");
            cancelar.hidden = !conCancelar;
            fondo.hidden = false;
            aceptar.focus();

            function cerrar(resultado) {
                fondo.hidden = true;
                aceptar.onclick = cancelar.onclick = null;
                document.removeEventListener("keydown", teclado);
                resolver(resultado);
            }
            function teclado(e) {
                if (e.key === "Escape") cerrar(false);
            }
            aceptar.onclick = function () { cerrar(true); };
            cancelar.onclick = function () { cerrar(false); };
            document.addEventListener("keydown", teclado);
        });
    }

    const Dialogo = {
        mostrar: function (t, m, tipo) { return abrir(t, m, tipo || "exito", false); },
        error: function (m) { return abrir("Datos no válidos", m, "error", false); },
        confirmar: function (m) { return abrir("Confirmar", m, "pregunta", true); }
    };
    window.Dialogo = Dialogo;

    // Mensaje que dejó el servidor (error del servicio o aviso de operación exitosa)
    const servidor = document.getElementById("mensaje-servidor");
    if (servidor && servidor.dataset.mensaje) {
        Dialogo.mostrar(servidor.dataset.titulo, servidor.dataset.mensaje, servidor.dataset.tipo);
    }

    // ---------- Validación ----------
    function cedulaValida(ced) {
        if (!/^\d{10}$/.test(ced)) return false;
        const provincia = parseInt(ced.substring(0, 2), 10);
        if (!((provincia >= 1 && provincia <= 24) || provincia === 30) || parseInt(ced[2], 10) >= 6) return false;
        let suma = 0;
        for (let i = 0; i < 9; i++) {
            let producto = parseInt(ced[i], 10) * (i % 2 === 0 ? 2 : 1);
            suma += producto > 9 ? producto - 9 : producto;
        }
        return (10 - suma % 10) % 10 === parseInt(ced[9], 10);
    }

    function validarCampo(campo) {
        if (campo.disabled || campo.closest("[hidden]")) return null;
        const valor = campo.value.trim();
        const nombre = campo.dataset.nombre || campo.name;
        if (campo.required && valor === "") return "El campo " + nombre + " es obligatorio.";
        if (valor === "") return null;
        switch (campo.dataset.validar) {
            case "cedula":
                return cedulaValida(valor) ? null : "La cédula debe tener 10 dígitos y un dígito verificador correcto.";
            case "entero": {
                const n = Number(valor);
                const min = campo.dataset.min ? Number(campo.dataset.min) : -Infinity;
                const max = campo.dataset.max ? Number(campo.dataset.max) : Infinity;
                if (!/^\d+$/.test(valor) || n < min || n > max) {
                    return "El campo " + nombre + " debe ser un número entero" +
                        (isFinite(max) ? " entre " + min + " y " + max : (isFinite(min) ? " mayor o igual a " + min : "")) + ".";
                }
                return null;
            }
            case "decimal":
                return /^\d+([.,]\d{1,2})?$/.test(valor) && Number(valor.replace(",", ".")) > 0
                    ? null : "El campo " + nombre + " debe ser un número mayor a cero con máximo dos decimales (ej.: 299.99).";
            default:
                return null;
        }
    }

    document.querySelectorAll("form").forEach(function (form) {
        form.addEventListener("submit", function (e) {
            const campos = form.querySelectorAll("input[name], select[name]");
            for (const campo of campos) {
                const problema = validarCampo(campo);
                campo.classList.toggle("invalido", problema !== null);
                if (problema) {
                    e.preventDefault();
                    Dialogo.error(problema).then(function () { campo.focus(); });
                    return;
                }
            }
            if (form.dataset.confirmar && !form.dataset.confirmado) {
                e.preventDefault();
                Dialogo.confirmar(form.dataset.confirmar).then(function (si) {
                    if (si) {
                        form.dataset.confirmado = "1";
                        form.submit();
                    }
                });
            }
        });
    });

    // Solo dígitos (y un separador decimal) en los campos numéricos
    document.querySelectorAll("[data-validar]").forEach(function (campo) {
        campo.addEventListener("input", function () {
            if (campo.dataset.validar === "decimal") {
                campo.value = campo.value.replace(/[^\d.,]/g, "").replace(/([.,].*)[.,]/g, "$1");
            } else {
                campo.value = campo.value.replace(/\D/g, "");
            }
            campo.classList.remove("invalido");
        });
    });

    // ---------- Carrito y resumen de la factura (solo en /facturar) ----------
    const formFactura = document.getElementById("form-factura");
    if (!formFactura) return;

    const DESCUENTO = 0.42;
    const TASA_MENSUAL = 0.165 / 12;
    const MAX_CELULARES = 10;
    const $ = function (id) { return document.getElementById(id); };
    const selCelular = $("codCelular");
    const txtCantidad = $("cantidad");
    const cuotas = $("numeroCuotas");
    const cuerpoCarrito = $("carrito").querySelector("tbody");
    const carrito = []; // { cod, texto, precio, cantidad }
    const moneda = function (v) {
        return "$" + v.toLocaleString("es-EC", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    };

    function formaPago() {
        return formFactura.querySelector("input[name=formaPago]:checked").value;
    }

    function datosCelular(cod) {
        const opcion = selCelular.querySelector("option[value='" + cod + "']");
        return opcion ? { cod: String(cod), texto: opcion.dataset.texto, precio: Number(opcion.dataset.precio) } : null;
    }

    // Si el celular ya está en el carrito se suman las cantidades (máximo 99 por celular)
    function agregar(cod, cantidad) {
        const datos = datosCelular(cod);
        if (!datos) return "El celular seleccionado ya no está disponible.";
        const existente = carrito.find(function (i) { return i.cod === datos.cod; });
        if (existente) {
            if (existente.cantidad + cantidad > 99) return "La cantidad de cada celular no puede superar 99.";
            existente.cantidad += cantidad;
        } else {
            if (carrito.length >= MAX_CELULARES) return "Una factura admite máximo " + MAX_CELULARES + " celulares distintos.";
            carrito.push(Object.assign(datos, { cantidad: cantidad }));
        }
        return null;
    }

    function pintarCarrito() {
        cuerpoCarrito.innerHTML = "";
        carrito.forEach(function (item, i) {
            const fila = document.createElement("tr");
            fila.innerHTML =
                "<td></td><td class='num'></td><td class='num'></td><td class='num'></td>" +
                "<td class='acciones'><button type='button' class='boton boton-peligro boton-chico'>Quitar</button>" +
                "<input type='hidden' name='itemCelular'><input type='hidden' name='itemCantidad'></td>";
            const celdas = fila.querySelectorAll("td");
            celdas[0].textContent = item.texto;
            celdas[1].textContent = moneda(item.precio);
            celdas[2].textContent = item.cantidad;
            celdas[3].textContent = moneda(Math.round(item.precio * item.cantidad * 100) / 100);
            fila.querySelector("input[name=itemCelular]").value = item.cod;
            fila.querySelector("input[name=itemCantidad]").value = item.cantidad;
            fila.querySelector("button").addEventListener("click", function () {
                carrito.splice(i, 1);
                pintarCarrito();
            });
            cuerpoCarrito.appendChild(fila);
        });
        $("carrito").hidden = carrito.length === 0;
        $("carrito-vacio").hidden = carrito.length > 0;
        actualizar();
    }

    function actualizar() {
        const credito = formaPago() === "CREDITO";
        $("campo-cuotas").hidden = !credito;
        $("r-etq-descuento").hidden = $("r-descuento").hidden = credito;
        $("r-etq-cuota").hidden = $("r-cuota").hidden = !credito;
        $("r-nota").hidden = !credito;
        $("r-etq-subtotal").textContent = credito ? "Monto a financiar" : "Subtotal";
        $("r-etq-total").textContent = credito ? "Total financiado" : "Total a pagar";

        const unidades = carrito.reduce(function (s, i) { return s + i.cantidad; }, 0);
        $("r-unidades").textContent = unidades;
        if (carrito.length === 0) {
            ["r-subtotal", "r-descuento", "r-cuota", "r-total"].forEach(function (id) { $(id).textContent = "—"; });
            return;
        }
        const subtotal = Math.round(carrito.reduce(function (s, i) { return s + i.precio * i.cantidad; }, 0) * 100) / 100;
        $("r-subtotal").textContent = moneda(subtotal);
        if (credito) {
            const n = parseInt(cuotas.value, 10);
            const cuota = subtotal * TASA_MENSUAL / (1 - Math.pow(1 + TASA_MENSUAL, -n));
            $("r-cuota").textContent = n + " × " + moneda(Math.round(cuota * 100) / 100);
            $("r-total").textContent = moneda(subtotal);
        } else {
            const descuento = Math.round(subtotal * DESCUENTO * 100) / 100;
            $("r-descuento").textContent = "− " + moneda(descuento);
            $("r-total").textContent = moneda(subtotal - descuento);
        }
    }

    $("btn-agregar").addEventListener("click", function () {
        if (!selCelular.value) {
            Dialogo.error("Seleccione el celular que desea agregar.").then(function () { selCelular.focus(); });
            return;
        }
        const cantidad = parseInt(txtCantidad.value, 10);
        if (!(cantidad >= 1 && cantidad <= 99)) {
            Dialogo.error("La cantidad debe ser un número entero entre 1 y 99.").then(function () { txtCantidad.focus(); });
            return;
        }
        const problema = agregar(selCelular.value, cantidad);
        if (problema) {
            Dialogo.error(problema);
            return;
        }
        selCelular.value = "";
        txtCantidad.value = "1";
        pintarCarrito();
    });

    // Al emitir, la venta debe tener al menos un celular
    formFactura.addEventListener("submit", function (e) {
        if (carrito.length === 0 && !e.defaultPrevented) {
            e.preventDefault();
            Dialogo.error("Agregue al menos un celular a la venta antes de emitir la factura.").then(function () { selCelular.focus(); });
        }
    });

    formFactura.addEventListener("change", actualizar);
    // Carrito que devolvió el servidor tras un error ("cod:cantidad,cod:cantidad")
    ($("carrito").dataset.carrito || "").split(",").forEach(function (par) {
        const partes = par.split(":");
        if (partes.length === 2) agregar(partes[0], parseInt(partes[1], 10));
    });
    pintarCarrito();

    // ---------- Autocompletar el nombre con la cédula (lo consulta la comercializadora a BanQuito) ----------
    // Si la cédula es de un cliente del banco, el nombre es el de BanQuito y el campo queda bloqueado.
    const txtCedula = $("cedula");
    const txtNombre = $("nombre");
    const ayuda = $("ayuda-cedula");
    let consulta = 0; // descarta respuestas de cédulas que ya se cambiaron

    function mostrarAyuda(texto, tipo) {
        ayuda.textContent = texto;
        ayuda.className = "nota ayuda-cedula" + (tipo ? " " + tipo : "");
    }

    function bloquearNombre(nombre) {
        txtNombre.value = nombre;
        txtNombre.readOnly = true;
        txtNombre.classList.add("bloqueado");
        txtNombre.classList.remove("invalido");
        txtNombre.title = "Nombre registrado en BanQuito";
    }

    // Quita el nombre que puso BanQuito (no lo que el usuario escribió en un campo libre)
    function liberarNombre() {
        if (txtNombre.readOnly) txtNombre.value = "";
        txtNombre.readOnly = false;
        txtNombre.classList.remove("bloqueado");
        txtNombre.title = "";
    }

    function buscarComprador() {
        const ced = txtCedula.value.trim();
        const actual = ++consulta;
        liberarNombre();
        if (ced.length < 10) {
            mostrarAyuda("", "");
            return;
        }
        if (!cedulaValida(ced)) {
            mostrarAyuda("La cédula no es válida: revise el dígito verificador.", "falla");
            return;
        }
        mostrarAyuda("Consultando a BanQuito…", "");
        fetch(txtCedula.dataset.buscar + "?cedula=" + encodeURIComponent(ced), { headers: { "Accept": "application/json" } })
            .then(function (r) { return r.json(); })
            .then(function (datos) {
                if (actual !== consulta) return; // ya escribieron otra cédula
                if (datos.encontrado) {
                    bloquearNombre(datos.nombre);
                    mostrarAyuda("✓ Cliente de BanQuito: nombre tomado del banco.", "ok");
                } else {
                    mostrarAyuda(datos.mensaje, "aviso");
                    txtNombre.focus();
                }
            })
            .catch(function () {
                if (actual !== consulta) return;
                mostrarAyuda("No se pudo consultar el nombre; escríbalo manualmente.", "aviso");
            });
    }

    txtCedula.addEventListener("input", buscarComprador);
    // Al cargar (y al volver con "Atrás", cuando el navegador restaura los campos) se consulta la cédula escrita
    window.addEventListener("pageshow", function () { if (txtCedula.value) buscarComprador(); });
})();
