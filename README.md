# 🏧 Cajero Automático — TP N° 2

Alumnos: 

De la Fuente Abdala Juan Ignacio - ISI
Crespo Thomas - ISI
Quintero Paez Samuel Alejo - ISI
Balverdi Santiago Nicolas - ISI


**Asignatura:** Programación III  
**Unidad:** 1 — Fundamentos y Desarrollo de POO  
**Institución:** DACEFyN — Universidad Nacional de La Rioja  

---

## 📋 Descripción

Simulador de cajero automático (ATM) desarrollado en Java, que procesa operaciones bancarias de manera secuencial con validaciones robustas, manejo de excepciones personalizadas y registro completo de transacciones.

El sistema permite gestionar múltiples cuentas bancarias, ejecutar operaciones como depósitos, extracciones y transferencias, y auditar cada movimiento con timestamp.

---

## 🗂️ Estructura del Proyecto

```
CajeroAutomatico/
└── src/main/java/
    ├── Main.java                               ← Punto de entrada + simulación del día
    ├── model/
    │   ├── CuentaBancaria.java                 ← Entidad principal con encapsulamiento estricto
    │   ├── Transaccion.java                    ← Registro inmutable de cada operación
    │   └── TipoTransaccion.java                ← ENUM: DEPÓSITO, EXTRACCIÓN, TRANSFERENCIA, CONSULTA
    ├── exception/
    │   ├── SaldoInsuficienteException.java     ← Saldo < monto solicitado
    │   ├── LimiteExtraccionExcedidoException.java ← Monto > $10.000 por operación
    │   ├── CuentaInactivaException.java        ← Cuenta desactivada
    │   └── PinInvalidoException.java           ← Acceso inválido
    ├── service/
    │   └── BancoService.java                   ← Lógica de negocio y gestión de cuentas
    ├── ui/
    │   └── CajeroUI.java                       ← Menú interactivo por consola
    └── util/
        ├── FormateadorMoneda.java              ← Formato $XXX.XXX,00
        └── Logger.java                         ← Auditoría con timestamps
```

---

## ⚙️ Funcionalidades Implementadas

- **Depósito:** valida monto positivo, actualiza saldo y registra la operación
- **Extracción:** valida saldo disponible y límite de $10.000 por operación
- **Transferencia:** operación atómica entre dos cuentas del sistema
- **Consulta de saldo:** sin modificar el estado de la cuenta
- **Historial:** últimas 10 transacciones con formato de auditoría
- **Menú interactivo:** navegación por consola con `switch expression` y validación de entradas

---

## 🚨 Excepciones Personalizadas

| Excepción | Condición |
|---|---|
| `SaldoInsuficienteException` | El saldo disponible es menor al monto solicitado |
| `LimiteExtraccionExcedidoException` | El monto supera los $10.000 permitidos por operación |
| `CuentaInactivaException` | Se intenta operar sobre una cuenta desactivada |
| `PinInvalidoException` | Intento de acceso con PIN incorrecto |

---

## 📊 Diagrama de Estados de una Cuenta

```
  ┌─────────┐    depositar/extraer/transferir    ┌──────────┐
  │  ACTIVA │ ──────────────────────────────────▶│  ACTIVA  │
  │         │           (operación OK)           │          │
  └────┬────┘                                    └──────────┘
       │
       │  desactivar()
       ▼
  ┌──────────┐
  │ INACTIVA │  ── No permite ninguna operación ──▶ CuentaInactivaException
  └────┬─────┘
       │
       │  (baja definitiva / administración)
       ▼
  ┌─────────┐
  │ CERRADA │  ── Estado terminal, sin retorno
  └─────────┘
```

---

## 📝 Formato de Auditoría

Cada operación queda registrada con el siguiente formato:

```
[2024-01-15 14:30:25] EXTRACCIÓN: $500,00 | Saldo: $2.450,00
[2024-01-15 14:31:02] TRANSFERENCIA: $8.000,00 | Saldo: $47.000,00
[2024-01-15 14:31:45] CONSULTA: Saldo: $11.000,00
```

---

## 🚀 Cómo Ejecutar

### Requisitos
- Java 17 o superior
- Ninguna dependencia externa

### Compilar
```bash
cd src/main/java
javac -d ../../../out util/FormateadorMoneda.java util/Logger.java model/TipoTransaccion.java model/Transaccion.java exception/*.java model/CuentaBancaria.java service/BancoService.java ui/CajeroUI.java Main.java
```

### Ejecutar
```bash
cd out
java Main
```

Al iniciar, el programa ejecuta automáticamente una **simulación de 15 transacciones** con 3 cuentas distintas, mostrando operaciones exitosas y el manejo de excepciones. Luego ofrece el modo interactivo por consola.

---

## 🧪 Simulación incluida en Main

La clase `Main` simula un día completo de operaciones con:

- **3 cuentas:** María García, Carlos López y Ana Rodríguez
- **15 transacciones variadas** incluyendo depósitos, extracciones, transferencias y consultas
- **Manejo de excepciones demostrado:**
  - `LimiteExtraccionExcedidoException` — intento de extraer $15.000
  - `CuentaInactivaException` — operación sobre cuenta desactivada
  - `SaldoInsuficienteException` — extracción mayor al saldo disponible

---

## 📐 Criterios cubiertos

| Aspecto | Implementación |
|---|---|
| Diseño y encapsulamiento | `numeroCuenta` declarado `final`, sin setters directos sobre saldo |
| Jerarquía de excepciones | 4 excepciones personalizadas con mensajes descriptivos |
| Lógica de negocio | Transferencia atómica, límites, validaciones completas |
| Interfaz de usuario | Menú con `switch expression`, `try-catch` para `InputMismatchException` |
| Estructura de paquetes | `model` / `exception` / `service` / `ui` / `util` |
| Logging y auditoría | Timestamp, tipo, monto y saldo resultante en cada operación |
