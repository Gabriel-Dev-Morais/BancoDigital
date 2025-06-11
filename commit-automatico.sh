#!/bin/bash
read -p "Digite um titulo para o commit: " titulo

git add .
git commit -m "$titulo"
