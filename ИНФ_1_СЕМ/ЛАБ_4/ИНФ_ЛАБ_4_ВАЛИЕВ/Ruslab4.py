#Чтение файла
with open('/Users/valie/OneDrive/Рабочий стол/lab4.json', 'r', encoding='utf-8') as file:
    lines = file.readlines()

#Учитываем табуляцию в файле
lines = [line.rstrip('\n') for line in lines]

#Преобразовываем массивы
for i in range(0, len(lines)):
    if lines[i][-1]=="[":
        s=len(lines[i+2])-len(lines[i+2].lstrip(" "))
        lines[i+2] = lines[i+2].replace('"', '- ', 1)
        lines[i+2] = lines[i+2].replace(" ", "", 1)

        for j in range(i+3, len(lines)-1):
            if  lines[j].count("},")>0 and len(lines[j])-len(lines[j].lstrip(" "))==s:
                lines[j+1]=lines[j+1].replace('"','- ', 1)
                lines[j+1]=lines[j+1].replace(" ", "", 1)

            elif len(lines[j])-len(lines[j].lstrip(" "))==s:
                lines[j] = lines[j].replace('"', ' ', 1)

#Убираем лишние символы
for i in range(0, len(lines)):
    if lines[i][-1]==",":
        lines[i]=lines[i][:-1]

    lines[i] = lines[i].replace('[', "")
    lines[i] = lines[i].replace(']', "")
    lines[i] = lines[i].replace("{", "")
    lines[i] = lines[i].replace("}", "")
    lines[i] = lines[i].replace('"', "")

#Убираем пустые строки
lines = [element for element in lines if any(c.isalpha() for c in element)]

#Убираем лишнюю табуляцию
a=lines[0].count(" ")
for i in range(0, len(lines)):
    lines[i]=lines[i].replace(" ", "", a)

#Создание файла и наполнение его переведенным форматом
with open("C:/Users/valie/OneDrive/Рабочий стол/lab4.yaml", 'w', encoding='utf-8') as file:
    file.write('\n '.join(lines) + '\n')
