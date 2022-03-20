
$('body').on('click', '.password-control', function(){
    if ($('#password').attr('type') === 'password'){
        $(this).addClass('view');
        $('#password').attr('type', 'text');
    } else {
        $(this).removeClass('view');
        $('#password').attr('type', 'password');
    }
    return false;
});

function sendJSON() {
    // с помощью jQuery обращаемся к элементам на странице по их именам
    let email = document.querySelector('#email');
    let role = document.querySelector('#role');
    // а вот сюда мы поместим ответ от сервера
    let result = document.querySelector('.result');
    // создаём новый экземпляр запроса XHR
    let xhr = new XMLHttpRequest();
    // адрес, куда мы отправим нашу JSON-строку
    let url = "http://localhost:8181/admin/role/add";
    // открываем соединение
    xhr.open("POST", url, true);
    // устанавливаем заголовок — выбираем тип контента, который отправится на сервер, в нашем случае мы явно пишем, что это JSON
    xhr.setRequestHeader("Content-Type", "application/json");
    // когда придёт ответ на наше обращение к серверу, мы его обработаем здесь
    xhr.onreadystatechange = function () {
        // если запрос принят и сервер ответил, что всё в порядке
        if (xhr.readyState === 4 && xhr.status === 200) {
            // выводим то, что ответил нам сервер — так мы убедимся, что данные он получил правильно
            result.innerHTML = this.responseText;
        }
    };
    // преобразуем наши данные JSON в строку
    var data = JSON.stringify({ "email": email.value, "role": role.value });
    // когда всё готово, отправляем JSON на сервер
    xhr.send(data);
}