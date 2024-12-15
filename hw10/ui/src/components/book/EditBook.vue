<template>
  <div>
    <div v-if="!submitted">
      <h2>Редактирование книги</h2>

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
        <button @click="updateBook" class="btn btn-success mx-2">Обновить</button>
        <button @click="deleteBook" class="btn btn-danger mx-2">Удалить</button>
      </div>
    </div>

    <div v-else>
      <div class="alert alert-success" role="alert" v-if="messageUpdate">
        {{ messageUpdate }}
      </div>

      <div class="alert alert-danger" role="alert" v-if="messageDelete">
        {{ messageDelete }}
      </div>

      <div class="alert alert-danger" role="alert" v-if="messageError">
        {{ messageError}}
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
  name: 'create-book',
  data() {
    return {
      book: {
        id: null,
        title: "",
        authorId: "",
        genreIds: [],
      },
      messageUpdate: '',
      messageDelete: '',
      messageError: '',
      genres: [],
      authors: [],
      submitted: false
    }
  },
  methods: {
    getBook(id) {
      BookDataService.getById(id)
          .then(response => {
            this.book = response.data
            this.book.id = response.data.id
            this.book.title = response.data.title
            this.book.authorId = response.data.author.id
            this.book.genreIds = response.data.genres.map(genre => genre.id);
          })
          .catch(e => {
            alert(e)
          })
    },

    updateBook() {
      console.log(this.book)
      BookDataService.updateById(this.book.id, this.book)
          .then(() => {
            this.messageUpdate = 'Книга была успешно обновлена'
            this.submitted = true;
          })
          .catch(e => {
            alert(e)
          })
    },

    deleteBook() {
      BookDataService.deleteById(this.book.id)
          .then(() => {
            this.messageDelete = 'Книга была успешно удалена'
            this.submitted = true;
          })
          .catch(e => {
            this.messageError = 'Ошибка сервера'
            console.log(e.message)
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
      this.book = {}
    }
  },

  created() {
    this.getGenres();
    this.getAuthors();
  },

  mounted() {
    this.getBook(this.$route.params.id)
  }
}
</script>