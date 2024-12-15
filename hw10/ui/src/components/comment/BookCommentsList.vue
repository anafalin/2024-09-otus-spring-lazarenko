<template>
  <div>
    <h2>Комментарии к книге</h2>
    <div>Название: {{bookTitle}}</div>

    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th scope="col">Текст</th>
        <th scope="col">Дата создания</th>
        <th scope="col">Действия</th>
      </tr>
      </thead>

      <tbody v-for="(comment) in comments" :key="comment.id">
      <tr>
        <td>{{ comment.text }}</td>
        <td>{{ comment.createdAt }}</td>
        <td>
          <a :href="'/comments/' + comment.id" class="btn btn-warning mx-2">Редактировать</a>
        </td>
      </tr>
      </tbody>
    </table>
    <a :href="'/comments/create/' + bookId" class="btn btn-success mx-2">Добавить комментарий</a>
  </div>
</template>

<script>

import CommentDataService from '../../services/CommentDataService';
import BookDataService from '../../services/BookDataService';

export default {
  name: 'CommentList',
  data() {
    return {
      bookId: null,
      bookTitle : null,
      comments: []
    }
  },

  methods: {
    getComments(bookId) {
      CommentDataService.getAllByBookId(bookId).then((response) => {
        this.comments = response.data;
      });
    },

    getBookTitle(bookId) {
      BookDataService.getById(bookId).then((response) => {
        this.bookTitle = response.data.title;
      })
    }
  },

  mounted() {
    let id = this.$route.query.bookId;
    this.bookId = id;
    this.getComments(id)
    this.getBookTitle(id)
  }
}

</script>