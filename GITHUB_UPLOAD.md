# 📤 Инструкция: Загрузка проекта на GitHub

## ✅ Проект готов к загрузке!

Проект уже инициализирован как Git репозиторий с первым коммитом.

---

## 🔧 Шаги для загрузки на GitHub

### 1️⃣ Создать репозиторий на GitHub

1. Перейди на https://github.com/new
2. Заполни форму:
   - **Repository name**: `TaskManagementSystem`
   - **Description**: `Complete REST API backend for Task Management System with Spring Boot 4.x, JWT auth, and PostgreSQL`
   - **Visibility**: Выбери `Public` или `Private` по своему желанию
3. **Не инициализируй** README, .gitignore или лицензию (у нас уже есть)
4. Нажми "Create repository"

### 2️⃣ Добавить remote и push на GitHub

Скопируй URL твоего репозитория и выполни команды в PowerShell:

```powershell
cd "C:\Users\samur\OneDrive\Desktop\TaskManagementSystem"

# Добавить remote (замени на свой URL)
git remote add origin https://github.com/YOUR_USERNAME/TaskManagementSystem.git

# Переименовать ветку в main (опционально)
git branch -M main

# Загрузить на GitHub
git push -u origin main
```

**Например:**
```powershell
git remote add origin https://github.com/ablaysharimov/TaskManagementSystem.git
git branch -M main
git push -u origin main
```

### 3️⃣ Аутентификация на GitHub

Если это первая загрузка, GitHub попросит аутентификацию. Выбери один из способов:

#### Вариант A: GitHub CLI (рекомендуется)
```powershell
gh auth login
# Выбери: GitHub.com
# На вопрос об аутентификации выбери: Paste an authentication token
# Создай token на https://github.com/settings/tokens/new
```

#### Вариант B: Personal Access Token (PAT)
1. Создай token: https://github.com/settings/tokens
   - Дай название: `TaskManagementSystem`
   - Выбери scope: `repo` (full control)
   - Сгенерируй и скопируй token
2. При запросе пароля введи token вместо пароля

#### Вариант C: SSH (продвинутый)
```powershell
# Если у тебя уже настроен SSH
git remote set-url origin git@github.com:YOUR_USERNAME/TaskManagementSystem.git
```

---

## 📊 Проверка загрузки

После успешной загрузки выполни:

```powershell
# Проверить remote
git remote -v

# Должно вывести:
# origin  https://github.com/YOUR_USERNAME/TaskManagementSystem.git (fetch)
# origin  https://github.com/YOUR_USERNAME/TaskManagementSystem.git (push)
```

Затем открой https://github.com/YOUR_USERNAME/TaskManagementSystem - должна увидеть свой проект! 🎉

---

## 🔄 Будущие обновления

После первой загрузки, для загрузки изменений просто используй:

```powershell
git add .
git commit -m "Описание изменений"
git push origin main
```

---

## 📝 Информация о репозитории

| Параметр | Значение |
|----------|----------|
| **Язык** | Java 21 |
| **Framework** | Spring Boot 4.0.6 |
| **БД** | PostgreSQL 15+ |
| **Файлы в репозитории** | 92 файла |
| **Начальный коммит** | ✅ Сделан (78be1ab) |

---

## ⚠️ Важные замечания

1. **Конфиденциальная информация**: Убедись, что в `.gitignore` отфильтрованы:
   - `logs/` ✅
   - `target/` (компилированный код) ✅
   - Database credentials - используй переменные окружения

2. **Если нужно исправить URL remote**:
   ```powershell
   # Удалить старый
   git remote remove origin
   
   # Добавить новый
   git remote add origin https://github.com/YOUR_USERNAME/TaskManagementSystem.git
   ```

3. **Если по ошибке загрузил, что не нужно**:
   ```powershell
   # Отменить последний commit (не отправленный)
   git reset --soft HEAD~1
   ```

---

## 🎯 Всё готово!

Проект полностью готов к загрузке. Просто следуй шагам выше и твой код будет на GitHub! 🚀

