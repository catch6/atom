#!/usr/bin/env bash
#
# Copyright (c) 2022-2023 Catch
# [Atom] is licensed under Mulan PSL v2.
# You can use this software according to the terms and conditions of the Mulan PSL v2.
# You may obtain a copy of Mulan PSL v2 at:
#          http://license.coscl.org.cn/MulanPSL2
# THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
# EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
# MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
# See the Mulan PSL v2 for more details.
#

# 使用示例
# ./atom.sh version 1.0.0
# ./atom.sh deploy 1.0.0

set -e

ACTION=$1
VERSION=$2

function info() {
  echo -e "\033[32m$1\033[0m"
}

function error() {
  echo -e "\033[31m$1\033[0m"
}

if [ "${ACTION}" == "version" ]; then
  info "参数校验..."
  if [ -z "${VERSION}" ] ;then
      error "请输入版本号! 示例: 1.0.0"
      exit
  fi
  info "参数校验...OK!\n"

  info "修改 README.md 版本号..."
  sed -i '' "s/<version>.*<\/version>/<version>${VERSION}<\/version>/g" README.md
  info "修改 README.md 版本号...OK!\n"

  info "更新 pom.xml 中 revision 版本号..."
  sed -i '' "s/<revision>.*<\/revision>/<revision>${VERSION}<\/revision>/g" pom.xml
  info "更新 pom.xml 中 revision 版本号...OK!\n"

  info "版本${VERSION}设置完成!"
elif [ "${ACTION}" == "deploy" ]; then
  info "参数校验..."
  if [ -z "${VERSION}" ] ;then
      error "请输入版本号! 示例: 1.0.0"
      exit
  fi
  info "参数校验...OK!\n"

  info "检查本地是否有代码未提交..."
  if [ -n "$(git status -s)" ] ;then
      error "本地有未提交的代码!"
      exit
  fi
  info "检查本地是否有代码未提交...OK!\n"

  info "切换到 main 分支..."
  git checkout main
  info "切换到 main 分支...OK!\n"

  info "拉取 main 分支..."
  git pull origin main
  info "拉取 main 分支...OK!\n"

  info "合并 2.x 分支到 main 分支..."
  git merge 2.x
  info "合并 2.x 分支到 main 分支...OK!\n"

  info "修改 README.md 版本号..."
  sed -i '' "s/<version>.*<\/version>/<version>${VERSION}<\/version>/g" README.md
  info "修改 README.md 版本号...OK!\n"

  info "更新 pom.xml 中 revision 版本号..."
  sed -i '' "s/<revision>.*<\/revision>/<revision>${VERSION}<\/revision>/g" pom.xml
  info "更新 pom.xml 中 revision 版本号...OK!\n"

  info "发布到 maven..."
  mvn clean deploy -DskipTests
  info "发布到 maven...OK!\n"

  info "提交代码并推送..."
  git add .
  git commit -m "发布 ${VERSION}" ||
  info "提交代码并推送...OK!\n"

  info "推送分支..."
  git push origin main
  info "推送分支...OK!\n"

  info "检查tag是否存在..."
  if git rev-parse "${VERSION}" >/dev/null 2>&1; then
    info "tag存在!"
    info "删除本地旧的 tag..."
    git tag -d "${VERSION}"
    info "删除本地旧的 tag...OK!\n"

    info "删除远程旧的 tag..."
    git push --delete origin "${VERSION}"
    info "删除远程旧的 tag...OK!\n"
  fi

  info "创建 tag..."
  git tag -a "${VERSION}" -m "${VERSION}"
  info "创建 tag...OK!\n"

  info "推送 tag..."
  git push origin "${VERSION}"
  info "推送 tag...OK!\n"

  info "切换到 2.x 分支..."
  git checkout 2.x
  info "切换到 2.x 分支...OK!\n"

  info "将 main 分支合并到 2.x 分支..."
  git merge main
  info "将 main 分支合并到 2.x 分支...OK!\n"

  info "推送 2.x 分支..."
  git push origin 2.x
  info "推送 2.x 分支...OK!\n"

  echo "进行下一版本的开发,请设置新版本version..."
fi

