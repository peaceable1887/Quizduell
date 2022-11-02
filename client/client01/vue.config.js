const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

/*module.exports = {
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        logLevel: 'debug',
        pathRewrite: { '^/api': '/' },
      },
    },
  },
}*/

module.exports = {
  devServer: {
    proxy:{
        target: "http://test.burmeister.hamburg:/",
    } 
  }
}