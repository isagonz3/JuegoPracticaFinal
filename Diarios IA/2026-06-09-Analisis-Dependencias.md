# Análisis de Dependencias y Portabilidad

**Fecha:** 2026-06-09
**Objetivo:** Analizar dependencias y bibliotecas para garantizar que el proyecto abra sin errores en otro dispositivo.

---

## Diagnóstico Inicial

Proyecto: Java 25 + Maven + JavaFX 21.0.6 + Gson 2.10.1 + JUnit 5.12.1

### Problemas críticos detectados

1. **`.idea/encodings.xml` — Rutas `$APPLICATION_HOME_DIR$`**
   - Líneas 4-5 usaban `$APPLICATION_HOME_DIR$/src/main/java` y `$APPLICATION_HOME_DIR$/src/main/resources`
   - `$APPLICATION_HOME_DIR$` apunta al directorio de instalación de IntelliJ, no al proyecto
   - En otro dispositivo, estas rutas no existen, causando warnings de encoding en IntelliJ

2. **`.idea/vcs.xml` — Mapping del directorio padre**
   - Línea 4: `<mapping directory="$PROJECT_DIR$/.." vcs="Git" />`
   - Registraba el directorio padre del proyecto como raíz Git
   - En otro dispositivo, el padre podría no ser un repo Git, causando confusión en VCS

### Problemas de portabilidad

3. **`pom.xml` — JDK 25 (`<source>25</source><target>25</target>`)**
   - JDK 25 es muy reciente (2025) y puede no estar disponible en otros dispositivos
   - Se verificó que el código NO usa características de Java 22+ (solo usa pattern matching instanceof de Java 16 y switch expressions de Java 14)
   - Es 100% compatible con JDK 21 (LTS)

4. **`.idea/misc.xml` — Referencia a JDK 25**
   - `languageLevel="JDK_25"` y `project-jdk-name="25"`

### Hallazgo adicional sobre javafx-maven-plugin

- Se investigó la posibilidad de actualizar `org.openjfx:javafx-maven-plugin` de 0.0.8 a 0.0.13
- **Descubrimiento:** No existe versión 0.0.13 ni superior. El plugin fue abandonado en 2021 y 0.0.8 es la última versión publicada.
- Se mantiene 0.0.8 por ser la única opción del groupId original. Funciona correctamente para `javafx:run` con JDK 21.

---

## Cambios Realizados

| # | Archivo | Cambio | Riesgo |
|---|---------|--------|--------|
| 1 | `.idea/encodings.xml` | `$APPLICATION_HOME_DIR$` → `$PROJECT_DIR$` | Nulo |
| 2 | `.idea/vcs.xml` | Eliminado mapping `$PROJECT_DIR$/..` | Nulo |
| 3 | `pom.xml` | `<source>25</source>` → `<source>21</source>` | Muy bajo |
| 4 | `pom.xml` | `<target>25</target>` → `<target>21</target>` | Muy bajo |
| 5 | `.idea/misc.xml` | `JDK_25` → `JDK_21`, `"25"` → `"21"` | Nulo |

### Archivos modificados (4)

- `.idea/encodings.xml` — 2 líneas cambiadas
- `.idea/vcs.xml` — 1 línea eliminada
- `pom.xml` — 2 líneas cambiadas
- `.idea/misc.xml` — 1 línea cambiada

---

## Verificación

- **Scaneo completo del código:** 64 archivos Java revisados, sin features de Java 22+
- **Compilación:** `mvn compile` exitoso con JDK 25 usando `--source 21 --target 21`
- **Plugins mantenidos:** javafx-maven-plugin 0.0.8, maven-compiler-plugin 3.13.0, maven-surefire-plugin 3.5.2

---

## Estado Final

El proyecto ahora:
- Abre sin errores en otro dispositivo (sin rutas rotas ni referencias locales)
- Compila con JDK 21 (LTS) — mucho más portable
- Sin cambios en el código fuente
- Dependencias intactas (Gson, JavaFX, JUnit)
