package bot.iservice.commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class comandosBoti extends ListenerAdapter { // Classe de comandos do bot

    public static void Logar (String mensagem) throws IOException { // Essa classe aqui loga as mensagens, grava e coloca no bloco de texto log.txt. Ela vai logar todas as perguntas que você fizer pro bot

        Path lugar = Paths.get("log.txt"); // Esse vai ser o arquivo que ele vai pegar para as mensagens

        if(!Files.exists(lugar)) { // Caso não existir, ele vai criar um

            Files.createDirectories(lugar);
        }
        File log = new File("log.txt"); // Esse vai ser o caminho que ele vai pegar para gravar as mensagens

        if(!log.exists()) {
            log.createNewFile();

        } // Caso não existir, ele vai criar um

        FileWriter fw = new FileWriter(log, true);

        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(mensagem);
        bw.newLine();

        bw.close();
        fw.close(); // Essas classes são para que ele gravar e escrever no bloco de texto, e sai depois de terminar

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) { // Esse são os slash commands. Não são importantes e não vou perder tempo explicado

        String repete = event.getName();
        String mensage = event.getMessageChannel().getAsMention();

        if (repete.equals("olá")) {
            String userTag = event.getUser().getAsTag();
            event.reply("Oi " + userTag + "!" ).queue();
        }
        else if (repete.equals("pergunta")) {

            OptionMapping opcao = event.getOption("responda");

            if (opcao == null) {
                event.reply("Algo deu errado. Não recebi nenhuma pergunta!").queue();
                return;
            }

            String pergunt = opcao.getAsString();

            int min = 1;
            int max = 6;

            int b = (int)(Math.random()*(max-min+1)+min);

            String resposta = "";

            switch(b) {

                case 1:
                    resposta = "Sim!";
                    break;
                case 2:
                    resposta = "Não";
                    break;
                case 3:
                    resposta = "Sim";
                    break;
                case 4:
                    resposta = "Sim?";
                    break;
                case 5:
                    resposta = "Não...";
                    break;
                case 6:
                    resposta = "Talvez";
                    break;
                default:
                    resposta = "Você fede";
                    break;
            }
            event.reply("Pergunta: " + pergunt + "R: " + resposta).queue();
        }

    } // Esse são os slash commands. Não são importantes e não vou perder tempo explicado

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("Ola", "Fala para o bot dizer oi para você!"));
        commandData.add(Commands.slash("pergunta", "Faz uma pergunta para o bot!")
                .addOption(OptionType.STRING, "responda", "a pergunta que você quer ser respondia", true));
        event.getGuild().updateCommands().addCommands(commandData).queue();

        // if (event.getGuild().getIdLong() == "1042601793663799306") {} //Commandos para servidores especiais

    } // isso tbm faz parte do slash commands e não vou explicar

    @Override
    public void onMessageReceived(MessageReceivedEvent event) { // Aqui que a magia acontece. Pra cada mensagem enviada no servidor, o bot ouve passivamente todos os conais e responde se algumas palavras chaves são respondidas
        String[] palavroes = {"buceta", "cu", "merda", "anus", "bicha", "boiola", "bosta", "caralho", "caraio", "filho da puta", "foda", "fodasse", "penis", "rola"}; // Aqui fica todos os palavroes que o bot vai excluir se ele encontra em uma mensagem
        String[] message = event.getMessage().getContentRaw().split(" "); // ele divide as mensagens em espaços para procurar facilmente
        String mensage2 = event.getMessage().getContentRaw(); // Aqui ele procura a mensagem por completo


        // Pattern - Ele vai pegar um regex com especificações, como o case_insensitive, e colocar em sua memoria
        // Matcher - Vai pegar o Pattern que está na memoria e ver se consegue achar o regex em uma string.

        //EXEMPLO

        /*
             nome do pattern                       palavra 1 para procurar         Palavra 2 para prcurar
                    |                                       |                              |
                    v                                       v                              v
        Pattern novoPattern = Pattern.compile("^(?=.*\\b"umaPalavraAqui"\\b)(?=.*\\b"outraPalavraAqui"\\b)(>?).*$", Pattern.CASE_INSENSITIVE);

        Não importa o posinamento das palavras. Se palavra 2 ficar primeiro e a palavra 1 ficar depois, ainda vai achar

        Você também pode procurar mais de duas palavras

        Pattern novoPattern2 = Pattern.compile("^(?=.*\\b"umaPalavraAqui"\\b)(?=.*\\b"outraPalavraAqui"\\b)(?=.*\\b"outraPalavraAqui"\\b)(?=.*\\b"outraPalavraAqui"\\b)(>?).*$", Pattern.CASE_INSENSITIVE);

        Como aqui

        Ou procurar apenas uma

        Pattern novoPattern3 = Pattern.compile("^(?=.*\\b"umaPalavraAqui"\\b)(>?).*$", Pattern.CASE_INSENSITIVE);

        (?=.*\\b"PalavraAqui"\\b) <-- para simplificar

        Matcher novoMatcher = novoPattern.matcher(mensage2);

        Dai só colocar em um if o matcher que você quer usar. Mais informações a baixo

        Nota: nunca mexa no mensage2. É ela que está a mensagem completa.
         */


        Pattern interrogacao1 = Pattern.compile("[^.!?]+\\?"); // Esse regex é o de qualquer mensagem que tiver ponto de interrogação
        Matcher interrogacao2 = interrogacao1.matcher(mensage2);
        Pattern recomendaFilme1 = Pattern.compile("^(?=.*\\brecomenda\\b)(?=.*\\bfilme\\b)(?=.*\\b?\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de recomendar um filme
        Matcher recomendaFilme2 = recomendaFilme1.matcher(mensage2);
        Pattern timeFavorito1 = Pattern.compile("^(?=.*\\btime\\b)(?=.*\\bfavorito\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o time favorito do bot
        Matcher timeFavorito2 = timeFavorito1.matcher(mensage2);
        Pattern timeFavorito3 = Pattern.compile("^(?=.*\\btime\\b)(?=.*\\btorce\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o time favorito do bot
        Matcher timeFavorito4 = timeFavorito3.matcher(mensage2);
        Pattern timeFavorito5 = Pattern.compile("^(?=.*\\btime\\b)(?=.*\\bjogou\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o time favorito do bot
        Matcher timeFavorito6 = timeFavorito5.matcher(mensage2);
        Pattern timeFavorito7 = Pattern.compile("^(?=.*\\bqual\\b)(?=.*\\btime\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o time favorito do bot
        Matcher timeFavorito8 = timeFavorito7.matcher(mensage2);
        Pattern favoritoFilme1 = Pattern.compile("^(?=.*\\bfilme\\b)(?=.*\\bfavorito\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o filme favorito do bot
        Matcher favoritoFilme2 = favoritoFilme1.matcher(mensage2);
        Pattern voceEumBot1 = Pattern.compile("^(?=.*\\bser\\b)(?=.*\\bbot\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // // Esse regex é o de perguntar pro bot de como ele é
        Matcher voceEumBot2 = voceEumBot1.matcher(mensage2);
        Pattern horas1 = Pattern.compile("^(?=.*\\bhoras\\b)(?=.*\\bsão\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar as horas
        Matcher horas2 = horas1.matcher(mensage2);
        Pattern horas3 = Pattern.compile("^(?=.*\\bhora\\b)(?=.*\\bagora\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar as horas
        Matcher horas4 = horas3.matcher(mensage2);
        Pattern horas5 = Pattern.compile("^(?=.*\\bsabe\\b)(?=.*\\bhoras\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar as horas
        Matcher horas6 = horas5.matcher(mensage2);
        Pattern dia1 = Pattern.compile("^(?=.*\\bdia\\b)(?=.*\\bhoje\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o dia
        Matcher dia2 = dia1.matcher(mensage2);
        Pattern dia3 = Pattern.compile("^(?=.*\\bdata\\b)(?=.*\\bhoje\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar o dia
        Matcher dia4 = dia3.matcher(mensage2);
        Pattern qualESeuNome1 = Pattern.compile("^(?=.*\\bqual\\b)(?=.*\\bnome\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de pergunta do nome do bot
        Matcher qualESeuNome2 = qualESeuNome1.matcher(mensage2);
        Pattern qualESeuNome3 = Pattern.compile("^(?=.*\\bcomo\\b)(?=.*\\bchama\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de pergunta do nome do bot
        Matcher qualESeuNome4 = qualESeuNome3.matcher(mensage2);
        Pattern ondeVoceMora1 = Pattern.compile("^(?=.*\\bonde\\b)(?=.*\\bmora\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex, igual o de baixo, é o de falar aonde o bot mora
        Pattern ondeVoceMora2 = Pattern.compile("^(?=.*\\baonde\\b)(?=.*\\bmora\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Pattern ondeVoceMora6 = Pattern.compile("^(?=.*\\bvocê\\b)(?=.*\\bestá\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Pattern ondeVoceMora5 = Pattern.compile("^(?=.*\\bmora\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher ondeVoceMora3 = ondeVoceMora1.matcher(mensage2);
        Matcher ondeVoceMora4 = ondeVoceMora2.matcher(mensage2);
        Matcher ondeVoceMora8 = ondeVoceMora5.matcher(mensage2);
        Matcher ondeVoceMora7 = ondeVoceMora6.matcher(mensage2);
        Pattern eDeOnde1 = Pattern.compile( "^(?=.*\\bé\\b)(?=.*\\bonde\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar aonde ele mora denovo, mais apenas com uma resposta
        Matcher eDeOnde2 = eDeOnde1.matcher(mensage2);
        Pattern eDeOnde3 = Pattern.compile( "^(?=.*\\bnasceu\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar aonde ele mora denovo, mais apenas com uma resposta
        Matcher eDeOnde4 = eDeOnde3.matcher(mensage2);
        Pattern trabalho1 = Pattern.compile("^(?=.*\\btrabalha\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex, igual os de baixo, é o de aonde o bot trabalha
        Matcher trabalho2 = trabalho1.matcher(mensage2);
        Pattern trabalho3 = Pattern.compile("^(?=.*\\btrabalho\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher trabalho4 = trabalho3.matcher(mensage2);
        Pattern trabalho5 = Pattern.compile("^(?=.*\\btrabalhava\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher trabalho6 = trabalho5.matcher(mensage2);
        Pattern trabalho7 = Pattern.compile("^(?=.*\\btrabalhou\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher trabalho8 = trabalho7.matcher(mensage2);
        Pattern torcePraQueTime1 = Pattern.compile("^(?=.*\\btorce\\b)(?=.*\\btime\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de qual time o bot torce
        Matcher torcePraQueTime2 = torcePraQueTime1.matcher(mensage2);
        Pattern bananaOuMaca1 = Pattern.compile("^(?=.*\\bbanana\\b)(?=.*\\bmaça\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala a fruta favorita do bot
        Matcher bananaOuMaca2 = bananaOuMaca1.matcher(mensage2);
        Pattern bananaOuMaca3 = Pattern.compile("^(?=.*\\bfruta\\b)(?=.*\\bfavorita\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala a fruta favorita do bot
        Matcher bananaOuMaca4 = bananaOuMaca3.matcher(mensage2);
        Pattern bananaOuMaca5 = Pattern.compile("^(?=.*\\bfruta\\b)(?=.*\\bgosta\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala a fruta favorita do bot
        Matcher bananaOuMaca6 = bananaOuMaca5.matcher(mensage2);
        Pattern corFavorita1 = Pattern.compile("^(?=.*\\bcor\\b)(?=.*\\bgosta\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala a COR favorita do bot
        Matcher corFavorita2 = corFavorita1.matcher(mensage2);
        Pattern corFavorita3 = Pattern.compile("^(?=.*\\bcor\\b)(?=.*\\bfavorita\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala a COR favorita do bot
        Matcher corFavorita4 = corFavorita3.matcher(mensage2);
        Pattern indeciso1 = Pattern.compile("^(?=.*\\bindeciso\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala que o bot é indeciso
        Matcher indeciso2 = indeciso1.matcher(mensage2);
        Pattern quemCriouVoce1 = Pattern.compile("^(?=.*\\bquem\\b)(?=.*\\bcriou\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala quem é o criador do bot
        Matcher quemCriouVoce2 = quemCriouVoce1.matcher(mensage2);
        Pattern quemCriouVoce3 = Pattern.compile("^(?=.*\\bquem\\b)(?=.*\\bcriado\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex fala quem é o criador do bot
        Matcher quemCriouVoce4 = quemCriouVoce3.matcher(mensage2);
        Pattern piada1 = Pattern.compile("^(?=.*\\bfala\\b)(?=.*\\bpiada\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de contar piadas, e o de baixo também
        Matcher piada2 = piada1.matcher(mensage2);
        Pattern piada3 = Pattern.compile("^(?=.*\\bcontar\\b)(?=.*\\bpiada\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher piada4 = piada3.matcher(mensage2);
        Pattern piada5 = Pattern.compile("^(?=.*\\bconta\\b)(?=.*\\bpiada\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher piada6 = piada5.matcher(mensage2);
        Pattern comidaFavorita1 = Pattern.compile("^(?=.*\\bcomida\\b)(?=.*\\bfavorita\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar a comida favorita do bot
        Matcher comidaFavorita2 = comidaFavorita1.matcher(mensage2);
        Pattern comidaFavorita3 = Pattern.compile("^(?=.*\\bcomida\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de falar a comida favorita do bot
        Matcher comidaFavorita4 = comidaFavorita3.matcher(mensage2);
        Pattern genero1 = Pattern.compile("^(?=.*\\bgenero\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o de genero do bot
        Matcher genero2 = genero1.matcher(mensage2);
        Pattern voceEumBotFaustao1 = Pattern.compile("^(?=.*\\bé\\b)(?=.*\\bbot\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esse regex é o que manda audio do faustão e o de baixo também
        Matcher voceEumBotFaustao2 = voceEumBotFaustao1.matcher(mensage2);
        Pattern voceEumBotFaustao3 = Pattern.compile("^(?=.*\\bÉ\\b)(?=.*\\bbot\\b)(>?).*$", Pattern.CASE_INSENSITIVE);
        Matcher voceEumBotFaustao4 = voceEumBotFaustao3.matcher(mensage2);
        Pattern ola1 = Pattern.compile("^(?=.*\\boi\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola2 = ola1.matcher(mensage2);
        Pattern ola3 = Pattern.compile("^(?=.*\\bOlá\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola4 = ola3.matcher(mensage2);
        Pattern ola5 = Pattern.compile("^(?=.*\\boi\\b)(?=.*\\btudo\\b)(?=.*\\bbem\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola6 = ola5.matcher(mensage2);
        Pattern ola7 = Pattern.compile("^(?=.*\\bola\\b)(?=.*\\btudo\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola8 = ola7.matcher(mensage2);
        Pattern ola9 = Pattern.compile("^(?=.*\\bOlá\\b)(?=.*\\btudo\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola10 = ola9.matcher(mensage2);
        Pattern ola11 = Pattern.compile("^(?=.*\\bOi\\b)(?=.*\\btudo\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola12 = ola11.matcher(mensage2);
        Pattern ola13 = Pattern.compile("^(?=.*\\bbom\\b)(?=.*\\bdia\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola14 = ola13.matcher(mensage2);
        Pattern ola15 = Pattern.compile("^(?=.*\\bboa\\b)(?=.*\\btarde\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola16 = ola15.matcher(mensage2);
        Pattern ola17 = Pattern.compile("^(?=.*\\bboa\\b)(?=.*\\bnoite\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola18 = ola17.matcher(mensage2);
        Pattern ola19 = Pattern.compile("^(?=.*\\bOla\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // Esses regexs são de saudações do bot
        Matcher ola20 = ola19.matcher(mensage2);
        Pattern niver1 = Pattern.compile("^(?=.*\\banos\\b)(?=.*\\btem\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // esse regex que faz o bot manda foto do vampeta
        Matcher niver2 = niver1.matcher(mensage2);
        Pattern niver3 = Pattern.compile("^(?=.*\\baniversário\\b)(?=.*\\bquando\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // esse regex que faz o bot manda foto do vampeta
        Matcher niver4 = niver3.matcher(mensage2);
        Pattern niver5 = Pattern.compile("^(?=.*\\baniversário\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // esse regex que faz o bot manda foto do vampeta
        Matcher niver6 = niver5.matcher(mensage2);
        Pattern niver7 = Pattern.compile("^(?=.*\\bquando\\b)(?=.*\\bnasceu\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // esse regex que faz o bot manda foto do vampeta
        Matcher niver8 = niver7.matcher(mensage2);
        Pattern vampetaFotos1 = Pattern.compile("^(?=.*\\bmanda\\b)(?=.*\\bfoto\\b)(>?).*$", Pattern.CASE_INSENSITIVE); // esse regex que faz o bot manda foto do vampeta
        Matcher vampetaFotos2 = vampetaFotos1.matcher(mensage2);







        boolean nope = false; // Esse borlean faz que mensagens são se repetem e que mensagens com ponto de interrogação não chamem o interrogacao2 para não repetir


        for (String s : message) { // esses fors olham toda a mensagem em partes e procura por palavroes. se encontrar, deleta ela e manda uma mensagem para ser educado
            for (String palavroe : palavroes) {
                if (s.equalsIgnoreCase(palavroe) || s.equalsIgnoreCase(palavroe + "?")) { // se achar, ele dá trigger nesse if
                    event.getMessage().delete().queue(); // ele deleta a mensagem aqui
                    event.getChannel().sendMessage("Seja Educado!").queue(); // e manda a mensagem de educação aqui
                    nope = true;
                }
            }

        }

        /*

        Massa!

        Você criou seu matcher e pattern. Agora usar em um if que o bot pode usar

        if (exemplo.matches() && !nope) {  // Agora vamos usar lo!

        quando um if é chamado, ele vai pegar um numero aleatorio de minimo x até maximo y

            int min = x;
            int max = y;

            int b = (int) (Math.random() * (max - min + 1) + min); // Ele vai fazer o calculo aqui e pegar um numero aleatorio daqui e gravar no b

            String resposta = " "; // Chamar uma String para gravar a resposta que der

            switch (b) { // No switch, ele vai pegar o case com o numero gravado do b. Quando mais case tem uma resposta, mais Peso ela tem!
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: // Essa resposta tem 10 de peso
                    resposta = "PPPPP";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16: // Essa tem 5
                    resposta = "PPPPPP";
                    break;
                case 17: case 18: case 19: case 20: // Essa tem 4
                    resposta = "PPPPPP";
                    break;
                case 21: case 22: // Essa tem 2, igual a de baixo
                    resposta = "PPPPPP";
                    break;
                case 23: case 24:
                    resposta = "PPPPPP";
                    break;
                case 25: case 26: case 27: // E Essa tem 3
                    resposta = "PPPPPP";
                    break;
                default: resposta = "Você fede"; // A resposta default não tem importancia. Não vai aparecer normalmente. Só quando der erro
                    break;
            }
            event.getChannel().sendMessage(resposta).queue(); // Depois de gravar, ela vai mandar a mensagem na string para esse objeto, que vai fazer o bot manda a mensagem
            try { // Esse try e catch salva as perguntas e respostas em um log. Uma das coisas que o adail pediu para implementar
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true; // Esse true é para que não repita nenhuma mensagem a mais que pode acontecer. Com a de interrogação2

            e feito! Divertasse com o bot!

        }

        template limpo
              |
              v

        if (exemplo.matches() && !nope) {

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "  ";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "  ";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "  ";
                    break;
                case 21: case 22:
                    resposta = "  ";
                    break;
                case 23: case 24:
                    resposta = "  ";
                    break;
                case 25: case 26: case 27:
                    resposta = "  ";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        Template limpo para mandar midias
                      |
                      v

        if (Exemplo1.matches() || exemplo2.matches() && !nope) {

            event.getChannel().sendFiles(FileUpload.fromData(Paths.get("CaminhoParaAImagem/Video/Audio/Arquivo"))).queue();
            try {
                Logar("Foi mandado um arquivo");
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

         */



        if (ola2.matches() || ola4.matches() || ola6.matches() || ola8.matches() || ola10.matches() || ola12.matches() || ola14.matches()
                || ola16.matches() || ola18.matches() || ola20.matches() && !nope) { // Esse if manda mensagem de olá se der true

            int min = 1;
            int max = 20;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Fala ae Campeão";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Olá!! ";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Tudo na boa";
                    break;
                default: resposta = "Tranquilo e favoravel";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }


        if (vampetaFotos2.matches() && !nope) { // Esse if manda mensagem com fotos do vampeta

            event.getChannel().sendMessage("Claro!").queue();

            int min = 1;
            int max = 30;

            int b = (int) (Math.random() * (max - min + 1) + min);

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta normal.png"))).queue();
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta com copa.png"))).queue();
                    break;
                case 17: case 18: case 19: case 20:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta com copa.png"))).queue();
                    break;
                case 21: case 22:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta normal.png"))).queue();
                    break;
                case 23: case 24:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta.png"))).queue();
                    break;
                case 25: case 26: case 27:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampeta com copa.png"))).queue();
                    break;
                case 28: case 29: case 30:
                    event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/vampetata.png"))).queue();
                    break;
                default:
                    System.out.println("Algo de errado");
                    break;
            }
            try {
                Logar("Foi mandado uma foto");
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (recomendaFilme2.matches() && !nope) { // Esse if manda mensagem de recomendar filmes

            event.getChannel().sendMessage("Claro!").queue();

                int min = 1;
                int max = 27;

                int b = (int) (Math.random() * (max - min + 1) + min);

                String resposta = " ";

                switch (b) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        resposta = "American Pie";
                        break;
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        resposta = "Projeto X";
                        break;
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                        resposta = "As branquelas";
                        break;
                    case 21:
                    case 22:
                        resposta = "Click Adam Sandler";
                        break;
                    case 23:
                    case 24:
                        resposta = "O virgem de 40 anos";
                        break;
                    case 25:
                    case 26:
                    case 27:
                        resposta = "Ted";
                        break;
                    default:
                        resposta = "Que filme Rapaz, vai trabalhar";
                        break;
                }
                ;
                event.getChannel().sendMessage(resposta).queue();
                try {
                    Logar(mensage2 + " " + resposta);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nope = true;
            }

        if (timeFavorito8.matches() ||timeFavorito2.matches() || timeFavorito4.matches() || timeFavorito6.matches() && !nope) { // Esse if manda mensagem de time favorito ou time que jogou

            event.getChannel().sendMessage("Sim!").queue();

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Coringão";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "No melhor do Brasil";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Timao porr*";
                    break;
                case 21: case 22:
                    resposta = "Palmeiras que não é";
                    break;
                case 23: case 24:
                    resposta = "Não me conhece mesmo";
                    break;
                case 25: case 26: case 27:
                    resposta = "Corinthias";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (favoritoFilme2.matches() && !nope) { // Esse if manda mensagem de filme favorito

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "É Sonic 2 - O Filme!";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "É Dragon Ball Super: Super Hero!";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "É 007 - Sem Tempo para Morrer";
                    break;
                case 21: case 22:
                    resposta = "É Toy Story 4";
                    break;
                case 23: case 24:
                    resposta = "É Star Wars: The Rise of Skywalker";
                    break;
                case 25: case 26: case 27:
                    resposta = "É Os Caras Malvados";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (voceEumBotFaustao2.matches() || voceEumBotFaustao4.matches() && !nope) { // Esse if manda um audio do faustão e fala que não é um bot

            event.getChannel().sendMessage("(ง •̀_•́)ง").queue();
            event.getChannel().sendFiles(FileUpload.fromData(Paths.get("src/main/java/bot/iservice/commands/midias/faustaoBotNao.mp3"))).queue();
            try {
                Logar("Foi mandado um audio");
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (voceEumBot2.matches() && !nope) { // Esse if manda mensagem de como é ser um bot

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "?kkkkkk";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Ta cas ideia errada";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Nem sei o que é isso";
                    break;
                case 21: case 22:
                    resposta = "Sim, navego todos os dias";
                    break;
                case 23: case 24:
                    resposta = "O que é bot maluco?";
                    break;
                case 25: case 26: case 27:
                    resposta = "Depende da quantidade de cerveja";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (horas6.matches() || horas4.matches() || horas2.matches() && !nope) { // Esse if manda mensagem do horario atual

            String resposta = " ";

            resposta = LocalTime.now().toString();

            event.getChannel().sendMessage("No meu Rolex, agora é exatamente " + resposta).queue();

            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (dia2.matches() || dia4.matches() && !nope) { // Esse if manda mensagem do dia atual

            String resposta = " ";

            resposta = LocalDate.now().toString();

            event.getChannel().sendMessage("Hoje é " + resposta).queue();

            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;
        }

        if (qualESeuNome2.matches() || qualESeuNome4.matches() && !nope) {  // Esse if manda mensagem do nome do bot

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Vampeta";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Marcos André Batista Santos";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Vampeta";
                    break;
                case 21: case 22:
                    resposta = "Ta escrito ai";
                    break;
                case 23: case 24:
                    resposta = "Depende do dia";
                    break;
                case 25: case 26: case 27:
                    resposta = "Marcos, porem me chamam de Vampeta";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (ondeVoceMora7.matches() || ondeVoceMora8.matches() || ondeVoceMora3.matches() || ondeVoceMora4.matches() && !nope) {  // Esse if manda mensagem de aonde o bot mora

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Sou Baiano";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Bahia, por que quer saber?";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Sou nomade";
                    break;
                case 21: case 22:
                    resposta = "Do lado do meu vizinho";
                    break;
                case 23: case 24:
                    resposta = "Na minha casa";
                    break;
                case 25: case 26: case 27:
                    resposta = "Salvador";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();

            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (eDeOnde4.matches() || eDeOnde2.matches() && !nope) {  // Esse if manda mensagem de aonde o bot mora, mais apenas com uma resposta

            int min = 1;
            int max = 2;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2:
                    resposta = "Nasci na Bahia";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }
            nope = true;
        }

        if (trabalho2.matches() || trabalho4.matches() || trabalho6.matches() || trabalho8.matches() && !nope) {  // Esse if manda mensagem de aonde o bot trabalha

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Sou Comentarista";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Era jogador, hoje sou comentarista";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Ja fiz de tudo, rs";
                    break;
                case 21: case 22:
                    resposta = "Comentarista e nos finais de semana trabalho com a noitada";
                    break;
                case 23: case 24:
                    resposta = "ex-futebolista brasileiro";
                    break;
                case 25: case 26: case 27:
                    resposta = "Depende da ocasiao";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (torcePraQueTime2.matches() && !nope) {  // Esse if manda mensagem de que time o bot torce

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Torço pro Flamengo!";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Torço pro Vasco!";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Torço pro Corínthias!";
                    break;
                case 21: case 22:
                    resposta = "Torço pro Palmeiras!";
                    break;
                case 23: case 24:
                    resposta = "Torço pro Grémio!";
                    break;
                case 25: case 26: case 27:
                    resposta = "Torço pro São Paulo!";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (bananaOuMaca2.matches() || bananaOuMaca4.matches() || bananaOuMaca6.matches() && !nope) {  // Esse if manda mensagem da fruta favorita do bot

            int min = 1;
            int max = 16;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Gosto mais de banana";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Depende, maçã";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (niver2.matches() || niver4.matches() || niver6.matches() || niver8.matches() && !nope) {  // Esse if manda mensagem do aniverssário

            int min = 1;
            int max = 16;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Nasci dia 13 de março de 1974";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "48, um jovem ainda";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (corFavorita2.matches() || corFavorita4.matches() && !nope) {  // Esse if manda mensagem da cor favorita do bot

            int min = 1;
            int max = 16;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Preto";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Rosa Choque";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }


        if (indeciso2.matches() && !nope) {  // Esse if manda mensagem do bot indeciso

            String resposta = "Eu sou bipolar tenha paciência comigo :pleading_face: :point_right: :point_left:";

            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (quemCriouVoce4.matches() || quemCriouVoce2.matches() && !nope) {  // Esse if manda mensagem de quem criou o bot

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Meus pais";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "a vida";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Deus";
                    break;
                case 21: case 22:
                    resposta = "Segredo!";
                    break;
                case 23: case 24:
                    resposta = "Minha Vó, Janete";
                    break;
                case 25: case 26: case 27:
                    resposta = "Nas ruas da Bahia";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (piada2.matches() || piada4.matches() || piada6.matches() && !nope) {  // Esse if manda mensagem de piada

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "sabe a diferença entre o poste, o bambu e uma mulher? kkkk pq eu não!";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Como um programador confunde um matemático? x = x + 1";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Por que um programador prefere o modo escuro? Porque a luz atrai bugs";
                    break;
                case 21: case 22:
                    resposta = "Quantos programadores de computador são necessários para trocar uma lâmpada? Nenhum, isso é um problema de hardware.";
                    break;
                case 23: case 24:
                    resposta = "Um programador foi preso por escrever código ilegível. Ele se recusou a comentar.";
                    break;
                case 25: case 26: case 27:
                    resposta = "Hoje ganhei meu primeiro dinheiro como programador. Eu vendi meu laptop";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (comidaFavorita2.matches() || comidaFavorita4.matches() && !nope) {  // Esse if manda mensagem de comida favorita

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Pizza de Peperroni!";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Bife Argentino!";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "File de Frango Grelhado!";
                    break;
                case 21: case 22:
                    resposta = "Kibe!";
                    break;
                case 23: case 24:
                    resposta = "Vinagrete!";
                    break;
                case 25: case 26: case 27:
                    resposta = "Pure de Batata!";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (genero2.matches() && !nope) { // Esse if manda mensagem de genero

            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    resposta = "Sou homem!";
                    break;
                case 11: case 12: case 13: case 14: case 15: case 16:
                    resposta = "Homem";
                    break;
                case 17: case 18: case 19: case 20:
                    resposta = "Eu não sei dizer ainda..";
                    break;
                case 21: case 22:
                    resposta = "Sou homem, na maior parte do tempo";
                    break;
                case 23: case 24:
                    resposta = "Homem sis";
                    break;
                case 25: case 26: case 27:
                    resposta = "Sou homem, cabra macho sim sinhô";
                    break;
                default: resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }

            nope = true;

        }

        if (interrogacao2.matches() && !nope) { // Esse if o de mensagem aleatorica com ponto de interrogacao. Sempre deixe ele no final.
            int min = 1;
            int max = 27;

            int b = (int) (Math.random() * (max - min + 1) + min);

            String resposta = " ";

            switch (b) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    resposta = "Claro ué";
                    break;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    resposta = "Siimm";
                    break;
                case 17:
                case 18:
                case 19:
                case 20:
                    resposta = "Concerteza";
                    break;
                case 21:
                case 22:
                    resposta = "Sim pow";
                    break;
                case 23:
                case 24:
                    resposta = "Não..";
                    break;
                case 25:
                case 26:
                case 27:
                    resposta = "Acho que não";
                    break;
                default:
                    resposta = "Você fede";
                    break;
            }
            event.getChannel().sendMessage(resposta).queue();
            try {
                Logar(mensage2 + " " + resposta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}