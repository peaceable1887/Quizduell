const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://test.burmeister.hamburg/',
        changeOrigin: true,
        logLevel: "debug",
      },
      '^/lobby-websocket': {
        target: 'http://test.burmeister.hamburg/',
        changeOrigin: true,
        logLevel: "debug",
        ws: true,
      },
      '^/quiz-websocket': {
        target: 'http://test.burmeister.hamburg/',
        changeOrigin: true,
        logLevel: "debug",
        ws: true,
      },
    },
  },
}
