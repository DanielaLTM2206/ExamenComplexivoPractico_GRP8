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

    // ---------- Resumen de la factura (solo en /facturar) ----------
    const formFactura = document.getElementById("form-factura");
    if (!formFactura) return;

    const DESCUENTO = 0.42;
    const TASA_MENSUAL = 0.165 / 12;
    const celular = document.getElementById("codCelular");
    const cantidad = document.getElementById("cantidad");
    const cuotas = document.getElementById("numeroCuotas");
    const $ = function (id) { return document.getElementById(id); };
    const moneda = function (v) {
        return "$" + v.toLocaleString("es-EC", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    };

    function formaPago() {
        return formFactura.querySelector("input[name=formaPago]:checked").value;
    }

    function actualizar() {
        const credito = formaPago() === "CREDITO";
        $("campo-cantidad").hidden = credito;
        $("campo-cuotas").hidden = !credito;
        $("r-etq-descuento").hidden = $("r-descuento").hidden = credito;
        $("r-etq-cuota").hidden = $("r-cuota").hidden = !credito;
        $("r-nota").hidden = !credito;
        $("r-etq-subtotal").textContent = credito ? "Monto a financiar" : "Subtotal";
        $("r-etq-total").textContent = credito ? "Total financiado" : "Total a pagar";

        const opcion = celular.options[celular.selectedIndex];
        const precio = opcion && opcion.dataset.precio ? Number(opcion.dataset.precio) : NaN;
        if (isNaN(precio)) {
            ["r-precio", "r-subtotal", "r-descuento", "r-cuota", "r-total"].forEach(function (id) { $(id).textContent = "—"; });
            return;
        }
        const cant = credito ? 1 : Math.max(1, parseInt(cantidad.value, 10) || 1);
        const subtotal = Math.round(precio * cant * 100) / 100;
        $("r-precio").textContent = moneda(precio);
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

    formFactura.addEventListener("input", actualizar);
    formFactura.addEventListener("change", actualizar);
    actualizar();
})();
