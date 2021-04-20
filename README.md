# ER-WorldGen
 Custom 1.16 world generation plugin for Minecraft servers

ER-WorldGen is a plugin to expand on vanilla world generation rather than completely replace it. It is written for PaperAPI but should work with Bukkit, Spigot, and other offshoots since it doesn't use Paper-specific calls. Since this is extending vanilla generation rather than replacing it, it requires reflection. I wasn't going to spend months working out the obsfucation for a side project, so I made use of the simple but effective WorldGeneratorAPI by rutgerkok https://github.com/rutgerkok/WorldGeneratorApi and his extension DoughWorldGenerator which modifies vanilla world gen.

# World Generation
With 66 all new biomes, I put my own spin on vanilla worlds by revamping resource distribution, visuals, caves (big, sprawling, underground sea!), oceans, and rivers. ER-WorldGen uses a custom structure file type which is used to place trees, ruins, dungeons, floating islands, and more. Rocks and bushes are generated procedurally, and some new biomes are squeezed in among the revamped vanilla ones. Alongside all new plantlife and structures, the world is much more lush and has an air of low-fantasy. Ores are distributed differently based on the biome, such as deserts being actual gold mines and swamps being packed with iron. Additionally, since this modifies vanilla generation it works overtop your own custom configuration params that were added in 1.12 and is update friendly as long as you compile for the relevant Paper and WorldGeneratorAPI version. This makes it very trivial to update to new versions of minecraft.

# Usage
This was written for personal use. Anyone is free to use this source however they wish, it will compile and function except your results will differ slightly - I do not included my structure files for trees, ruins, etc. here. There are commands included in the plugin which work similar to WorldEdit to selection, save, and load structures which you can then place into the plugin's folder to start appearing in your world. If you want the structure files, feel free to reach out and I can send them your way.

That aside, I'm working on a cleaner version with some configuration to publish. Currently waiting for MC 1.17 to take advantage of the new caves and world height.

# Screenshots
![2021-04-06_01 34 00](https://user-images.githubusercontent.com/14932139/115466958-c7283c00-a1fe-11eb-915d-e896f3b69c44.png)
![2021-02-06_01 12 25](https://user-images.githubusercontent.com/14932139/115467812-1b7feb80-a200-11eb-8a46-66b36f84cf19.png)
![2021-02-06_00 43 45](https://user-images.githubusercontent.com/14932139/115467825-1fac0900-a200-11eb-8151-149a73513281.png)
![2021-01-29_22 00 20](https://user-images.githubusercontent.com/14932139/115468139-a365f580-a200-11eb-8ab9-2ab95acc51d1.png)
![2021-02-05_15 48 43](https://user-images.githubusercontent.com/14932139/115467835-23d82680-a200-11eb-8b50-079bcae8f145.png)
![2021-02-05_21 12 09](https://user-images.githubusercontent.com/14932139/115467841-263a8080-a200-11eb-80f6-fb599c8f8dc4.png)
![2021-02-05_15 42 44](https://user-images.githubusercontent.com/14932139/115467851-29357100-a200-11eb-90c5-176087b09386.png)
![2021-02-03_18 26 05](https://user-images.githubusercontent.com/14932139/115467853-2b97cb00-a200-11eb-9477-5aaf335888d3.png)
![2021-02-03_15 42 16](https://user-images.githubusercontent.com/14932139/115467858-2dfa2500-a200-11eb-99b3-72ae4f8cb800.png)
![2021-02-03_02 22 59](https://user-images.githubusercontent.com/14932139/115467873-36526000-a200-11eb-90f3-30d0fc7ecf35.png)
![2021-02-01_18 52 57](https://user-images.githubusercontent.com/14932139/115467883-394d5080-a200-11eb-978b-695b5d71b6b6.png)
![2021-02-01_03 43 53](https://user-images.githubusercontent.com/14932139/115467893-3ce0d780-a200-11eb-94b6-a5bfef0d4f18.png)
![2021-02-01_02 53 08](https://user-images.githubusercontent.com/14932139/115467905-423e2200-a200-11eb-9759-74e88560e1cc.png)
![2021-02-01_02 53 32](https://user-images.githubusercontent.com/14932139/115467923-466a3f80-a200-11eb-895d-f758b4e8c7dc.png)
![2021-02-01_02 50 54](https://user-images.githubusercontent.com/14932139/115467933-4bc78a00-a200-11eb-9c77-814b7632a966.png)
![2021-02-01_02 48 34](https://user-images.githubusercontent.com/14932139/115467955-52560180-a200-11eb-881b-164b9e77d4b5.png)
![2021-01-24_00 00 02](https://user-images.githubusercontent.com/14932139/115468000-66016800-a200-11eb-98d4-e796eff94891.png)
