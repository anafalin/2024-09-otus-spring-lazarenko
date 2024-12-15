<template>
  <h2 class="mb-5 text-center">Доступные книги</h2>

  <div class="container col-10">

    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Название</th>
        <th scope="col">Автор</th>
        <th scope="col">Жанры</th>
        <th scope="col">Действия</th>
      </tr>
      </thead>

      <tbody v-for="(book) in books" :key="book.id">
      <tr>
        <td>{{ book.id }}</td>
        <td>{{ book.title }}</td>
        <td>{{ book.author.fullName }}</td>
        <td>
          <ul v-for="(genre) in book.genres" :key="genre.id">
            <li>{{ genre.name }}</li>
          </ul>
        </td>
        <td>
          <a :href="'/books/' + book.id" class="btn btn-warning mx-2">Редактировать</a>
          <button @click="goToComments(book.id)" class="btn btn-primary mx-2">Комментарии</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import BookDataService from '../../services/BookDataService';

export default {
  name: 'BooksList',
  data() {
    return {
      books: []
    }
  },
  methods: {
    getBooks() {
      BookDataService.getAll().then((response) => {
        this.books = response.data;
      });
    },

    goToComments(id) {
      this.$router.push({ name: 'comments', query: { bookId: id } });
    }
  },

  created() {
    this.getBooks();
  }
}

</script>