# Magnet Search

[![Apache License 2.0][1]][2]
[![Platform][3]][4]
[![Release][100]]()

## Introduce 简介
Magnet Search 是一个搜索磁力链接的爬虫应用
## Features 特性
* 1、基本遵循Google Material Design设计风格。
* 2、Jsoup爬虫爬取DHT网站信息。

## Issues 反馈
如果有任何问题，请到提交[issues][21]。

If you have any questions, please write to [the issue][21] .

## Structure 项目结构
```
```

### Version 版本号

<主版本>.<次版本>.<修复版本>.<日期版本><versionCode版本> [-dev]

*主版本* 大版本更新，如功能大改、ui界面大改

*次版本* 小版本更新，增加小功能，ui小修改

*修复版本* 修复bug更新

*日期版本* 发布日期，发布渠道等

*-dev* 开发版

*versionCode* 根据git commit次数生成

```
eg:
0.1.2.17063011-play
```

## Screenshots 截屏
![]()
![]()
## 下载
[Google Play][20]
## 日志

# Build 构建

Windows

    > git clone https://github.com/zkywalker/MagnetSearch.git
    > cd MagnetSearch.git
    > gradlew app:assembleDebug

Linux

    $ git clone https://github.com/zkywalker/MagnetSearch.git
    $ cd MagnetSearch.git
    $ ./gradlew app:assembleDebug

生成的 apk 文件在 app\build\outputs\apk 目录下


## Dependencies 依赖


    $ ./gradlew allDeps

## License 许可
```
Copyright (C) 2017 Keral

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]:https://img.shields.io/:license-apache-blue.svg
[2]:https://www.apache.org/licenses/LICENSE-2.0.html
[3]:https://img.shields.io/badge/platform-Android-blue.svg
[4]:https://www.android.com/

[100]:https://img.shields.io/badge/release-1.3.2-blue.svg

[20]:https://play.google.com/store/apps/details?id=org.zky.tool.magnetsearch

[21]:https://github.com/zkywalker/MagnetSearch/issues


http://www.cilisou.cn/s.php?q=a~b~&encode_=1
TODO 完善异常捕捉，更多源