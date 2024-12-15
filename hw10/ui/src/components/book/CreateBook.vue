<template>
  <div>
    <div v-if="!submitted">
      <h2>Создание книги</h2>

      <div class="mb-3">
        <div>Название</div>
        <input type="text" class="form-control" id="title" required name="title" v-model="book.title">
      </div>

      <div class="mb-3">
        <div>Автор</div>
        <select required name="author" v-model="book.authorId" class="form-select">
          <option v-for="(author) in authors" :key="author.id" :value="author.id">{{ author.fullName }}</option>
        </select>
      </div>

      <div class="mb-3">
        <div>Жанры</div>
        <select required multiple name="genres" v-model="book.genreIds" class="form-select">
          <option v-for="(genre) in genres" :key="genre.id" :value="genre.id">{{ genre.name }}</option>
        </select>
      </div>

      <div class="mb-3">
        <button @click="saveBook" class="btn btn-primary my-2">Сохранить</button>
        <div class="alert alert-danger" role="alert" v-if="messageError">
          {{ messageError }}
        </div>
      </div>
    </div>

    <div v-else>
      <div class="alert alert-success" role="alert" v-if="messageCreate">
        {{ messageCreate }}
      </div>

      <div class="alert alert-danger" role="alert" v-if="messageError">
        {{ messageError }}
      </div>

      <button @click="newBook" class="btn btn-primary">Добавить новую книгу</button>
    </div>
  </div>
</template>

<script>
import BookDataService from '../../services/BookDataService'
import GenreDataService from '../../services/GenreDataService'
import AuthorDataService from "@/services/AuthorDataService";

export default {
  name: 'add-book',
  data() {
    return {
      book: {
        id: null,
        title: "",
        authorId: "",
        genreIds: [],
      },
      genres: [],
      authors: [],
      submitted: false,
      messageError: '',
      messageCreate: ''
    }
  },
  methods: {
    saveBook() {
      this.messageError = ''; // Сброс сообщения об ошибке перед проверкой

      // Проверка на незаполненность полей
      if (!this.book.title) {
        this.messageError = 'Название обязательно для заполнения.';
        return;
      }
      if (!this.book.authorId) {
        this.messageError = 'Автор обязателен для выбора.';
        return;
      }
      if (this.book.genreIds.length === 0) {
        this.messageError = 'Необходимо выбрать хотя бы один жанр.';
        return;
      }

      var data = {
        title: this.book.title,
        authorId: this.book.authorId,
        genreIds: this.book.genreIds,
      }

      BookDataService.create(data)
          .then(response => {
            this.book.id = response.data.id
            this.messageCreate = "Книга успешно создана!"
            this.submitted = true;
          })
          .catch(e => {
            this.messageError = 'Ошибка сервера';
            console.log(e.message);
          })
    },

    getGenres(){
      GenreDataService.getAll().then((response) => {
        this.genres = response.data;
      });
    },

    getAuthors(){
      AuthorDataService.getAll().then((response) => {
        this.authors = response.data;
      });
    },

    newBook() {
      this.submitted = false
      this.book = {
        id: null,
        title: "",
        authorId: "",
        genreIds: [],
      }
      this.messageError = '';
      this.messageCreate = '';
    }
  },

  created() {
    this.getGenres();
    this.getAuthors();
  }
}
</script>
