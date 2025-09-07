function formatNumberWithCommas(value) {
  const num = value.replace(/\D/g, '');
  return num.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function handleInputWithCommas(event) {
  const input = event.target;
  const cursorPos = input.selectionStart;
  const rawValue = input.value.replace(/,/g, '');
  const formatted = formatNumberWithCommas(rawValue);
  input.value = formatted;

  const diff = formatted.length - rawValue.length;
  input.setSelectionRange(cursorPos + diff, cursorPos + diff);
}

function calcularPresupuesto() {
  const ingresosInput = document.getElementById('ingresos');
  const egresosInput = document.getElementById('egresos');
  const metaInput = document.getElementById('meta');

  if (!ingresosInput || !egresosInput || !metaInput) {
    console.error("❌ Los campos del simulador no están disponibles.");
    return;
  }

  // ✅ Limpieza robusta: solo dígitos
  const ingresos = parseInt(ingresosInput.value.replace(/[^\d]/g, ''), 10) || 0;
  const egresos = parseInt(egresosInput.value.replace(/[^\d]/g, ''), 10) || 0;
  const meta = parseInt(metaInput.value.replace(/[^\d]/g, ''), 10) || 0;

  const disponible = ingresos - egresos;
  const ahorro = disponible * 0.3;
  const gastoLibre = disponible * 0.7;

  let resultado = `
    ✅ Ingresos: $${formatNumberWithCommas(ingresos.toString())}<br>
    ❌ Egresos: $${formatNumberWithCommas(egresos.toString())}<br>
    📌 Disponible: $${formatNumberWithCommas(disponible.toString())}<br>
    💡 Sugerencia: Ahorrar: $${formatNumberWithCommas(ahorro.toFixed(0))}, Gasto libre: $${formatNumberWithCommas(gastoLibre.toFixed(0))}
  `;

  if (meta > 0 && ahorro > 0) {
    const meses = Math.ceil(meta / ahorro);
    resultado += `<br>🎯 Con este ahorro alcanzarías tu meta de $${formatNumberWithCommas(meta.toString())} en ${meses} meses.`;
  }

  document.getElementById('resultado').innerHTML = resultado;
}

setTimeout(() => {
  ['ingresos', 'egresos', 'meta'].forEach(id => {
    const input = document.getElementById(id);
    if (input) {
      input.setAttribute('type', 'text');
      input.addEventListener('input', handleInputWithCommas);
    }
  });

  const calcularBtn = document.getElementById('Calcular');
  if (calcularBtn) {
    calcularBtn.addEventListener('click', calcularPresupuesto);
  }
}, 300);
