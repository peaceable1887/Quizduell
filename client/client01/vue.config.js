const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer: {
    proxy: {
      '^/': {
        target: 'http://test.burmeister.hamburg/api',
        changeOrigin: true,
        logLevel: "debug",
        pathRewrite: {"^/api" : "/"}
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
