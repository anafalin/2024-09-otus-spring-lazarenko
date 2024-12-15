<template>
  <div>
    <div v-if="!submitted">
      <h2>Создание комментария</h2>

      <div class="mb-3">
        <div>Дата создания</div>
        <div>{{ createdAt }}</div>
      </div>

      <div class="mb-3">
        <textarea class="form-control" id="title" required name="title" v-model="comment.text"></textarea>
      </div>

      <div class="mb-3">
        <button @click="saveComment" class="btn btn-success mx-2">Сохранить</button>
      </div>
    </div>

    <div v-else>
      <div class="alert alert-success" role="alert" v-if="messageCreate">
        {{ messageCreate }}
      </div>

      <div class="alert alert-danger" role="alert" v-if="messageError">
        {{ messageError }}
      </div>
    </div>
  </div>
</template>

<script>
import CommentDataService from '../../services/CommentDataService';

export default {
  name: 'update-comment',
  data() {
    return {
      comment: {
        id: null,
        text: "",
        bookId: null,
      },
      createdAt: '',
      messageCreate: '',
      messageError: '',
      submitted: false
    }
  },
  methods: {
    saveComment() {
      var data = {
        text: this.comment.text,
        bookId: this.comment.bookId,
      }
      console.log(data)
      CommentDataService.create(data)
          .then(response => {
            this.comment.id = response.data.id
            this.messageCreate = "Комментарий успешно добавлен!"
            this.submitted = true;
          })
          .catch( e => {
            this.messageError = 'Ошибка сервера'
            console.log(e.message)
          })
    },

    setCreatedAt() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1);
      const day = String(date.getDate());

      this.createdAt = `${year}-${month}-${day}`;
    }
  },

  mounted() {
    this.comment.bookId =  this.$route.params.id;
    this.setCreatedAt();
  }
}
</script>