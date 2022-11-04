const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

/*module.exports = {
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://test.burmeister.hamburg/',
        changeOrigin: true,
        logLevel: 'debug',
      },
    },
  },
}*/

module.exports = {
  devServer: {
    proxy: 'http://test.burmeister.hamburg/'
  }
}

