const webpack = require('webpack');

const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        'VUE_PROD_HYDRATION_MISMATCH_DETAILS': JSON.stringify(false),
      }),
    ],
  },

  devServer: {
    port: 8081
  }
}