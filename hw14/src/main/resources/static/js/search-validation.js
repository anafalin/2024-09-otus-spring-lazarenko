document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.querySelector('input[type="search"]');
    const searchButton = document.querySelector('button[type="submit"]');

    // Функция для проверки, заполнено ли поле
    function toggleButtonState() {
        searchButton.disabled = !searchInput.value.trim();  // Отключаем кнопку, если поле пустое
    }

    // Применяем проверку при вводе текста в поле
    searchInput.addEventListener("input", toggleButtonState);

    // Проверка при загрузке страницы (например, если поле уже пустое)
    toggleButtonState();
});