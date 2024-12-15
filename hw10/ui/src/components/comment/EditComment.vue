<template>
  <div>
    <div v-if="!submitted">
      <h2>Редактирование комментария</h2>

      <div class="mb-3">
        <div>Дата создания</div>
        <div>{{ comment.createdAt }}</div>
      </div>

      <div class="mb-3">
        <textarea class="form-control" id="title" required name="title" v-model="comment.text"></textarea>
      </div>

      <div class="mb-3">
        <button @click="updateComment" class="btn btn-success mx-2">Обновить</button>
        <button @click="deleteComment" class="btn btn-danger mx-2">Удалить</button>
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
        {{ messageError }}
      </div>
    </div>
  </div>
</template>

<script>
import CommentDataService from '../../services/CommentDataService';
import BookDataService from '../../services/BookDataService';

export default {
  name: 'update-comment',
  data() {
    return {
      comment: {
        id: null,
        text: "",
        createdAt: "",
        bookId: null,
      },
      bookTitle: "",
      messageUpdate: '',
      messageDelete: '',
      messageError: '',
      submitted: false
    }
  },
  methods: {
    getComment(id) {
      CommentDataService.getById(id).then(response => {
        this.comment.id = response.data.id
        this.comment.text = response.data.text
        this.comment.createdAt = response.data.createdAt
        this.comment.bookId = response.data.bookId
        this.getBookTitle(response.data.bookId);
      })
    },

    getBookTitle(bookId) {
      BookDataService.getById(bookId).then((response) => {
        this.bookTitle = response.data.title;
      })
    },

    updateComment() {
      console.log(this.comment);
      CommentDataService.updateById(this.comment.id, this.comment)
          .then(() => {
            this.messageUpdate = 'Комментарий был успешно обновлен'
            this.submitted = true;
          })
          .catch((e) => {
            this.messageError = 'Ошибка сервера'
            console.log(e.message)
          })
    },

    deleteComment() {
      CommentDataService.deleteById(this.comment.id)
          .then(() => {
            this.messageDelete = 'Комментарий был успешно удален'
            this.submitted = true;
          })
          .catch((e) => {
            this.messageError = 'Ошибка сервера'
            console.log(e.message)
          })
    },
  },

  mounted() {
    let commentIdFromRequest = this.$route.params.id;
    this.getComment(commentIdFromRequest);
  }
}
</script>