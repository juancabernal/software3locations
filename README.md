# EatUpAPI 🍽️

Bienvenido al repositorio central de la aplicación **EatUp**. Este proyecto se desarrolla bajo una arquitectura de **microservicios** como parte de la materia **Software 3** en la **Universidad Católica de Oriente (UCO)**.

---

## 📌 Información del Proyecto
* **Semestre:** 2026-1
* **Institución:** Universidad Católica de Oriente (Rionegro, Antioquia)
* **Materia:** Software 3
* **Docente:** NOREÑA BLANDÓN JUAN PABLO

---

<p align="center">
  <img src="https://github.com/andrias01/EatUpComercial/blob/ed922f89f45dd21a74d886adc5c307154612e2f2/imagenesReadme/flujoGitHub.png" alt="FLUJO DE TRABAJO GITHUB" width="800">
</p>

## 🚀 FLUJO DE TRABAJO GITHUB - PROYECTO

```bash
# 1️⃣ Clonar repositorio
git clone <URL_DEL_REPOSITORIO>

# 2️⃣ Entrar al proyecto
cd nombre-del-proyecto

# 3️⃣ Cambiar a rama de desarrollo
git checkout dev

# 4️⃣ Crear rama para nueva funcionalidad
git checkout -b nombreDeRama

# Verificar rama actual
git branch

# ==========================================
# 🔎 ANTES DE SUBIR CAMBIOS (IMPORTANTE)
# ==========================================

# Ver si hay cambios nuevos en el repositorio remoto
git fetch

# Revisar qué cambios hicieron otros compañeros
git log --oneline --graph --all
# o Tambian
git loc

# Si no afecta tu trabajo, actualizar tu rama
git pull origin develop

# ==========================================
# 📦 PREPARAR CAMBIOS
# ==========================================

# Ver qué archivos cambiaron
git status

# Agregar cambios al stage
git add .

# Crear commit con mensaje claro y detallado
git commit -m "Descripción clara y detallada de la funcionalidad implementada"

# Subir rama al repositorio
git push -u origin nombreDeRama

# ==========================================
# 🔁 CREAR PULL REQUEST
# ==========================================
# Ir a GitHub
# Crear Pull Request hacia la rama develop
# En el repositorio en la nube debe aparcer Compare & pull request aqui poner un buen titulo y una excelente descripción
# Ya listo lo anterior dar click en Create pull request esperar que gitHub compare esa rama con la develop con el fin de no encontrar un conflicto.
# Esperar revisión asignando al profesor como aprovador IMPORTENTE 
# Puedo seguir con ese nombre de rama de formar local para seguir realizando nuevas funcionalidades y seguir el mismo flujo.
